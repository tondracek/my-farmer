package com.tondracek.myfarmer.ui.mainshopscreen.shopsmapview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLngBounds
import com.tondracek.myfarmer.core.domain.domainerror.DomainError
import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import com.tondracek.myfarmer.core.domain.usecaseresult.getOrElse
import com.tondracek.myfarmer.location.model.Location
import com.tondracek.myfarmer.location.model.meters
import com.tondracek.myfarmer.location.usecase.measureMapDistanceNotNull
import com.tondracek.myfarmer.map.GetMapViewInitialCameraBoundsUC
import com.tondracek.myfarmer.map.GetUserLocationUC
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ShopsMapViewModel @Inject constructor(
    getShopsByDistancePaged: GetShopsByDistancePagedUC,
    getAllShopsPaginated: GetAllShopsPaginatedUC,
    applyFilters: ApplyFiltersUC,
    getUserLocation: GetUserLocationUC,
    getClosestShops: GetClosestShopsUC,
    getShopFilters: GetShopFiltersUC,
    getUsersByIds: GetUsersByIdsUC,
    private val getInitialCameraBounds: GetMapViewInitialCameraBoundsUC,
) : ViewModel() {

    private val filters: StateFlow<ShopFilters> =
        getShopFilters(ShopFiltersRepositoryKeys.MAIN_SHOPS_SCREEN)

    private val userLocation: Flow<Location?> = getUserLocation()
        .distinctUntilChanged()

    private val _shops: Flow<Collection<Shop>> = userLocation
        .scan<Location?, Location?>(null) { lastAccepted, current ->
            if (lastAccepted == null) return@scan current
            if (current == null) return@scan lastAccepted

            val d = measureMapDistanceNotNull(lastAccepted, current)

            if (d > 50.meters) current else lastAccepted
        }
        .distinctUntilChanged()
        .combine(filters) { location, filters -> location to filters }
        .flatMapLatest { (location, filters) ->
            when {
                location != null -> channelFlow {
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
                }

                else -> channelFlow {
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
                }
            }
        }

    private val _shopOwners: Flow<List<SystemUser>> = _shops
        .flatMapLatest {
            val ownerIds = it.map(Shop::ownerId).distinct()
            getUsersByIds(ownerIds)
        }
        .getOrEmitError(emptyList())
        .distinctUntilChanged()

    private val _closestShops: Flow<List<Shop>> = _shops
        .map { getClosestShops(it, count = 3) }
        .distinctUntilChanged()

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
        _effects.emit(ShopsMapViewEffect.OpenShopDetail(shopId))
    }

    private val _effects = MutableSharedFlow<ShopsMapViewEffect>(extraBufferCapacity = 1)
    val effects: SharedFlow<ShopsMapViewEffect> = _effects

    private fun <T> Flow<DomainResult<T>>.getOrEmitError(defaultValue: T): Flow<T> = map { result ->
        result
            .withFailure { _effects.emit(ShopsMapViewEffect.ShowError(it.error)) }
            .getOrElse(defaultValue)
    }
}

sealed interface ShopsMapViewEffect {

    data class OpenShopDetail(val shopId: ShopId) : ShopsMapViewEffect

    data class ShowError(val error: DomainError) : ShopsMapViewEffect
}