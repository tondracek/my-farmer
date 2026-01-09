package com.tondracek.myfarmer.ui.mainshopscreen.shopsmapview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLngBounds
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.core.usecaseresult.getOrElse
import com.tondracek.myfarmer.map.GetMapViewInitialCameraBoundsUC
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.usecase.GetAllShopsUC
import com.tondracek.myfarmer.shop.domain.usecase.GetClosestShopsUC
import com.tondracek.myfarmer.shopfilters.domain.model.ShopFilters
import com.tondracek.myfarmer.shopfilters.domain.usecase.GetShopFiltersUC
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import com.tondracek.myfarmer.systemuser.domain.usecase.GetUsersByIdsUC
import com.tondracek.myfarmer.ui.common.shop.filter.ShopFiltersRepositoryKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ShopsMapViewModel @Inject constructor(
    getAllShops: GetAllShopsUC,
    getClosestShops: GetClosestShopsUC,
    getShopFilters: GetShopFiltersUC,
    getUsersByIds: GetUsersByIdsUC,
    private val getInitialCameraBounds: GetMapViewInitialCameraBoundsUC,
) : ViewModel() {

    private val filters: StateFlow<ShopFilters> =
        getShopFilters(ShopFiltersRepositoryKeys.MAIN_SHOPS_SCREEN)

    private val _shops: Flow<Collection<Shop>> = filters
        .flatMapLatest { getAllShops(filters = it) }
        .getOrEmitError(emptyList())
        .distinctUntilChanged()

    private val _shopOwners: Flow<List<SystemUser>> = _shops
        .flatMapLatest {
            val ownerIds = it.map(Shop::ownerId).distinct()
            getUsersByIds(ownerIds)
        }
        .getOrEmitError(emptyList())
        .distinctUntilChanged()

    private val _closestShops: Flow<List<Shop>> = _shops
        .map { getClosestShops(it, count = 5) }

    private val _initialCameraBounds: Flow<LatLngBounds?> = _closestShops
        .map { shops ->
            val locations = shops.map { it.location.toLatLng() }
            getInitialCameraBounds(locations)
        }

    private val _shopUiItems: Flow<Set<ShopMapItem>> = combine(
        _shops,
        _shopOwners,
    ) { shops, shopOwners ->
        val ownersById = shopOwners.associateBy { it.id }
        shops.mapNotNull {
            val owner = ownersById[it.ownerId] ?: return@mapNotNull null
            it.toShopMapItem(owner.profilePicture)
        }.toSet()
    }

    val state: StateFlow<ShopsMapViewState> = combine(
        _shopUiItems,
        _initialCameraBounds,
    ) { shopUiItems, initialCameraBounds ->
        ShopsMapViewState(
            shops = shopUiItems,
            initialCameraBounds = initialCameraBounds,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = ShopsMapViewState()
    )

    fun onShopSelected(shopId: ShopId) = viewModelScope.launch {
        _effects.emit(ShopsMapViewEvent.OpenShopDetail(shopId))
    }

    private val _effects = MutableSharedFlow<ShopsMapViewEvent>(extraBufferCapacity = 1)
    val effects: SharedFlow<ShopsMapViewEvent> = _effects

    private fun <T> Flow<UCResult<T>>.getOrEmitError(defaultValue: T): Flow<T> = map { result ->
        result.getOrElse {
            viewModelScope.launch {
                _effects.emit(ShopsMapViewEvent.ShowError(it.userError))
            }
            defaultValue
        }
    }
}

sealed interface ShopsMapViewEvent {

    data class OpenShopDetail(val shopId: ShopId) : ShopsMapViewEvent

    data class ShowError(val message: String) : ShopsMapViewEvent
}