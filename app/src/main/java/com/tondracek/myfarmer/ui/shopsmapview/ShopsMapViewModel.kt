package com.tondracek.myfarmer.ui.shopsmapview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.tondracek.myfarmer.common.image.usecase.GetCustomMarkerIcon
import com.tondracek.myfarmer.common.usecase.GetByIdsUC
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.core.usecaseresult.combineUCResults
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.usecase.GetAllShopsUC
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import com.tondracek.myfarmer.ui.core.navigation.AppNavigator
import com.tondracek.myfarmer.ui.core.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ShopsMapViewModel @Inject constructor(
    getAllShops: GetAllShopsUC,
    getByIdsUC: GetByIdsUC<SystemUser>,
    getCustomMarkerIcon: GetCustomMarkerIcon,
    private val navigator: AppNavigator,
) : ViewModel() {

    private val _shops: Flow<UCResult<Set<Shop>>> = getAllShops()
        .map { it.map { shopsList -> shopsList.toSet() } }

    private val _shopMarkerIcons: Flow<UCResult<Map<ShopId, BitmapDescriptor?>>> = _shops
        .map {
            val shops = it.getOrNull() ?: return@map emptyList<ShopId>()
            shops.map { shop -> shop.ownerId }
        }
        .flatMapLatest { getByIdsUC(it) }
        .transform { result ->
            val list = result.getOrNull() ?: emptyList()

            val descriptors = coroutineScope {
                list.map { user ->
                    async {
                        user.id to getCustomMarkerIcon(user.profilePicture.uri)
                    }
                }.awaitAll().toMap()
            }

            emit(UCResult.Success(descriptors))
        }


    val state: StateFlow<ShopsMapViewState> = combineUCResults(
        _shops,
        _shopMarkerIcons,
        { ShopsMapViewState() }
    ) { shops, markerIcons ->
        val shopUiItems = shops.map {
            it.toShopMapItem(markerIcons[it.ownerId])
        }.toSet()

        ShopsMapViewState(
            shops = shopUiItems,
            initialCameraBounds = getLatLngBounds(shops.map { it.location.toLatLng() })
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

private fun getLatLngBounds(locations: Collection<LatLng>): LatLngBounds? {
    if (locations.isEmpty()) return null

    val builder = LatLngBounds.builder()
    locations.forEach { builder.include(it) }
    return builder.build()
}