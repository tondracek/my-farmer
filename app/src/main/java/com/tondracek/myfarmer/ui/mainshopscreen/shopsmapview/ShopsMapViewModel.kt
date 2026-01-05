package com.tondracek.myfarmer.ui.mainshopscreen.shopsmapview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.core.usecaseresult.combineUCResults
import com.tondracek.myfarmer.core.usecaseresult.flatMapSuccess
import com.tondracek.myfarmer.core.usecaseresult.mapSuccessFlow
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
import com.tondracek.myfarmer.ui.core.navigation.AppNavigator
import com.tondracek.myfarmer.ui.core.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ShopsMapViewModel @Inject constructor(
    getAllShops: GetAllShopsUC,
    getClosestShops: GetClosestShopsUC,
    getShopFilters: GetShopFiltersUC,
    getUsersByIds: GetUsersByIdsUC,
    private val getInitialCameraBounds: GetMapViewInitialCameraBoundsUC,
    private val navigator: AppNavigator,
) : ViewModel() {

    private val filters: StateFlow<ShopFilters> =
        getShopFilters(ShopFiltersRepositoryKeys.MAIN_SHOPS_SCREEN)

    private val _shops: Flow<UCResult<Collection<Shop>>> = filters
        .flatMapLatest { getAllShops(filters = it) }

    private val _shopOwners: Flow<UCResult<List<SystemUser>>> = _shops
        .flatMapSuccess {
            val ownerIds = it.map(Shop::ownerId).distinct()
            getUsersByIds(ownerIds)
        }

    private val _closestShops: Flow<UCResult<List<Shop>>> = _shops
        .mapSuccessFlow { getClosestShops(it, count = 5) }

    val state: StateFlow<ShopsMapViewState> = combineUCResults(
        _shops,
        _closestShops,
        _shopOwners,
        { ShopsMapViewState() }
    ) { shops, closestShops, shopOwners ->
        val shopUiItems = shops.mapNotNull {
            val owner = shopOwners.find { user -> user.id == it.ownerId } ?: return@mapNotNull null
            it.toShopMapItem(owner.profilePicture)
        }.toSet()

        val initialCameraBounds =
            getInitialCameraBounds(closestShops.map { it.location.toLatLng() })

        ShopsMapViewState(
            shops = shopUiItems,
            initialCameraBounds = initialCameraBounds,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = ShopsMapViewState()
    )

    fun onShopSelected(shopId: ShopId) {
        navigator.navigate(Route.ShopBottomSheetRoute(shopId.toString()))
    }
}