package com.tondracek.myfarmer.ui.mainshopscreen.shopsmapview

import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLngBounds
import com.tondracek.myfarmer.core.domain.domainerror.DomainError
import com.tondracek.myfarmer.core.domain.usecaseresult.getOrElse
import com.tondracek.myfarmer.core.domain.usecaseresult.withFailure
import com.tondracek.myfarmer.location.model.Location
import com.tondracek.myfarmer.location.model.meters
import com.tondracek.myfarmer.map.GetMapViewInitialCameraBoundsUC
import com.tondracek.myfarmer.map.GetUserApproximateLocationUC
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.repository.DistancePagingCursor
import com.tondracek.myfarmer.shop.domain.usecase.GetAllShopsPaginatedUC
import com.tondracek.myfarmer.shop.domain.usecase.GetClosestShopsUC
import com.tondracek.myfarmer.shop.domain.usecase.GetShopsByDistancePagedUC
import com.tondracek.myfarmer.shopfilters.domain.model.ShopFilters
import com.tondracek.myfarmer.shopfilters.domain.usecase.ApplyFiltersUC
import com.tondracek.myfarmer.shopfilters.domain.usecase.GetShopFiltersUC
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import com.tondracek.myfarmer.systemuser.domain.usecase.GetUsersByIdsUC
import com.tondracek.myfarmer.ui.common.shop.filter.ShopFiltersRepositoryKeys
import com.tondracek.myfarmer.ui.core.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ShopsMapViewModel @Inject constructor(
    private val getShopsByDistancePaged: GetShopsByDistancePagedUC,
    private val getAllShopsPaginated: GetAllShopsPaginatedUC,
    private val applyFilters: ApplyFiltersUC,
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

    private val _shops: Flow<Collection<Shop>> = combine(
        userApproximateLocation,
        filters,
        refreshTrigger,
    ) { location, filters, _ ->
        location to filters
    }.flatMapLatest { (location, filters) ->
        when (location) {
            null -> getAllShopsPagingFLow(filters)
            else -> getShopsByDistancePagingFlow(location, filters)
        }
    }

    private val _shopOwners: Flow<List<SystemUser>> = _shops
        .flatMapLatest {
            val ownerIds = it.map(Shop::ownerId).distinct()
            getUsersByIds(ownerIds)
        }
        .withFailure { emitEffect(ShopsMapViewEffect.ShowError(it.error)) }
        .getOrElse(emptyList())
        .distinctUntilChanged()

    private val shopUiItems: Flow<Set<ShopMapItem>> = combine(
        _shops,
        _shopOwners,
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
            val closestShopLocations = _shops
                .map { getClosestShops(it, count = 3) }
                .first()
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

    private fun getShopsByDistancePagingFlow(
        location: Location,
        filters: ShopFilters
    ): Flow<Collection<Shop>> = channelFlow {
        _isLoading.emit(true)

        val shops = mutableSetOf<Shop>()
        var cursor: DistancePagingCursor? = null

        do {
            getShopsByDistancePaged(
                center = location,
                pageSize = 20,
                cursor = cursor,
            ).withSuccess { (list, nextCursor) ->
                val filtered = applyFilters.sync(shops = list, filters = filters)
                shops.addAll(filtered)

                send(shops.toList())
                cursor = nextCursor
            }
        } while (cursor != null)

        _isLoading.emit(false)
    }

    private fun getAllShopsPagingFLow(filters: ShopFilters): Flow<Collection<Shop>> = channelFlow {
        _isLoading.emit(true)

        val shops = mutableSetOf<Shop>()
        var cursor: ShopId? = null

        do {
            getAllShopsPaginated(
                limit = 20,
                after = cursor
            ).withSuccess {
                val filtered = applyFilters.sync(shops = it, filters = filters)
                shops.addAll(filtered)

                send(shops.toList())
                cursor = it.lastOrNull()?.id
            }
        } while (cursor != null)

        _isLoading.emit(false)
    }
}

sealed interface ShopsMapViewEffect {

    data class OpenShopDetail(val shopId: ShopId) : ShopsMapViewEffect

    data class ShowError(val error: DomainError) : ShopsMapViewEffect
}