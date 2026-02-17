package com.tondracek.myfarmer.ui.mainshopscreen.shopsmapview

import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLngBounds
import com.tondracek.myfarmer.core.domain.domainerror.DomainError
import com.tondracek.myfarmer.core.domain.domainresult.getOrElse
import com.tondracek.myfarmer.core.domain.domainresult.withFailure
import com.tondracek.myfarmer.location.domain.model.meters
import com.tondracek.myfarmer.location.domain.usecase.GetMapViewInitialCameraBoundsUC
import com.tondracek.myfarmer.location.domain.usecase.GetUserApproximateLocationUC
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.usecase.GetClosestShopsUC
import com.tondracek.myfarmer.shopfilters.domain.model.ShopFilters
import com.tondracek.myfarmer.shopfilters.domain.usecase.GetShopFiltersUC
import com.tondracek.myfarmer.ui.common.shop.filter.ShopFiltersRepositoryKeys
import com.tondracek.myfarmer.ui.core.viewmodel.BaseViewModel
import com.tondracek.myfarmer.ui.mainshopscreen.shopsmapview.logic.ObserveMapShopsStream
import com.tondracek.myfarmer.user.domain.model.SystemUser
import com.tondracek.myfarmer.user.domain.usecase.GetUsersByIdsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ShopsMapViewModel @Inject constructor(
    private val observeMapShopsStream: ObserveMapShopsStream,
    getUserApproximateLocation: GetUserApproximateLocationUC,
    getClosestShops: GetClosestShopsUC,
    getShopFilters: GetShopFiltersUC,
    getUsersByIds: GetUsersByIdsUC,
    private val getInitialCameraBounds: GetMapViewInitialCameraBoundsUC,
) : BaseViewModel<ShopsMapViewEffect>() {

    private val _selectedShopId = MutableStateFlow<ShopId?>(null)
    val selectedShopId: StateFlow<ShopId?> = _selectedShopId

    private val _isLoading = MutableStateFlow(false)

    private val filters: StateFlow<ShopFilters> =
        getShopFilters(ShopFiltersRepositoryKeys.MAIN_SHOPS_SCREEN)

    private val userApproximateLocation = getUserApproximateLocation(50.meters)

    private val _refreshTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    private val refreshTrigger = _refreshTrigger.onStart { emit(Unit) }

    private val shops: SharedFlow<Set<Shop>> = combine(
        userApproximateLocation,
        filters,
        refreshTrigger,
    ) { location, filters, _ ->
        location to filters
    }.flatMapLatest { (location, filters) ->
        observeMapShopsStream(location = location, filters = filters)
            .onStart { _isLoading.update { true } }
            .onCompletion { _isLoading.update { false } }
    }.shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        replay = 1
    )

    private val shopOwners: Flow<List<SystemUser>> = shops
        .map { it.map(Shop::ownerId).toSet() }
        .distinctUntilChanged()
        .flatMapLatest { ownerIds -> getUsersByIds(ownerIds.toList()) }
        .withFailure { emitEffect(ShopsMapViewEffect.ShowError(it.error)) }
        .getOrElse(emptyList())

    private val shopUiItems: Flow<Set<ShopMapItem>> = combine(
        shops,
        shopOwners,
    ) { shops, shopOwners ->
        val ownersById = shopOwners.associateBy { it.id }
        shops.mapNotNull {
            val owner = ownersById[it.ownerId] ?: return@mapNotNull null
            it.toShopMapItem(owner.profilePicture)
        }.toSet()
    }

    private val selectedShop: Flow<ShopMapItem?> = combine(
        _selectedShopId,
        shopUiItems,
    ) { selectedShopId, shopUiItems ->
        shopUiItems.firstOrNull { it.id == selectedShopId }
    }.distinctUntilChanged()

    private val initialCameraBounds = MutableStateFlow<LatLngBounds?>(null)

    val state: StateFlow<ShopsMapViewState> = combine(
        shopUiItems,
        _isLoading,
        selectedShop,
        initialCameraBounds,
    ) { shopUiItems, isLoading, selectedShop, initialCameraBounds ->
        ShopsMapViewState(
            shops = shopUiItems,
            isLoading = isLoading,
            selectedShop = selectedShop,
            initialCameraBounds = initialCameraBounds,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = ShopsMapViewState.Empty,
    )

    init {
        viewModelScope.launch {
            val loadedShops = shops.first { it.isNotEmpty() }
            val closestShopLocations = getClosestShops(loadedShops, count = 3)
                .map { it.location.toLatLng() }
            val bounds = getInitialCameraBounds(closestShopLocations)

            initialCameraBounds.emit(bounds)
        }
    }

    fun onShopSelected(shopId: ShopId) = viewModelScope.launch {
        emitEffect(ShopsMapViewEffect.OpenShopDetail(shopId))
        _selectedShopId.update { shopId }
    }

    fun refreshShops() =
        viewModelScope.launch { _refreshTrigger.emit(Unit) }

    fun onShopDeselected() = _selectedShopId.update { null }
}

sealed interface ShopsMapViewEffect {

    data class OpenShopDetail(val shopId: ShopId) : ShopsMapViewEffect

    data class ShowError(val error: DomainError) : ShopsMapViewEffect
}