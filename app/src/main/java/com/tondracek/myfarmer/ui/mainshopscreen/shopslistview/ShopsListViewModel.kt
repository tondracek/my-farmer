package com.tondracek.myfarmer.ui.mainshopscreen.shopslistview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.core.usecaseresult.getOrElse
import com.tondracek.myfarmer.location.model.Distance
import com.tondracek.myfarmer.location.model.Location
import com.tondracek.myfarmer.location.usecase.measureMapDistance
import com.tondracek.myfarmer.map.GetUserLocationUC
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.usecase.GetAverageRatingsByShopUC
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.usecase.GetAllShopsUC
import com.tondracek.myfarmer.shopfilters.domain.model.ShopFilters
import com.tondracek.myfarmer.shopfilters.domain.usecase.GetShopFiltersUC
import com.tondracek.myfarmer.ui.common.shop.filter.ShopFiltersRepositoryKeys
import com.tondracek.myfarmer.ui.mainshopscreen.shopslistview.components.ShopListViewItem
import com.tondracek.myfarmer.ui.mainshopscreen.shopslistview.components.toListItem
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

@HiltViewModel
class ShopsListViewModel @Inject constructor(
    getAverageRatingsByShopUC: GetAverageRatingsByShopUC,
    getUserLocation: GetUserLocationUC,
    getAllShops: GetAllShopsUC,
    getShopFilters: GetShopFiltersUC,
) : ViewModel() {

    private val filters: StateFlow<ShopFilters> =
        getShopFilters(ShopFiltersRepositoryKeys.MAIN_SHOPS_SCREEN)

    private val userLocation: Flow<Location?> = getUserLocation()
        .getOrElse(null) { _effects.emit(ShopsListViewEffect.ShowError(it.userError)) }
        .distinctUntilChanged()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val shops: Flow<List<Shop>> = filters
        .flatMapLatest { filters -> getAllShops(filters = filters) }
        .getOrElse(emptyList()) {
            _effects.emit(ShopsListViewEffect.ShowError(it.userError))
        }

    private val _distanceToShops: Flow<Map<ShopId, Distance?>> = combine(
        shops,
        userLocation
    ) { shops, userLocation ->
        shops.associate { shop ->
            val distance = measureMapDistance(shop.location, userLocation)
            shop.id to distance
        }
    }.distinctUntilChanged()

    private val averageRatings: Flow<Map<ShopId, Rating>> = getAverageRatingsByShopUC()
        .getOrElse(emptyMap()) { _effects.emit(ShopsListViewEffect.ShowError(it.userError)) }
        .distinctUntilChanged()

    private val shopsUiData: Flow<List<ShopListViewItem>> = combine(
        shops,
        _distanceToShops,
        averageRatings
    ) { shops, distances, ratings ->
        shops.map { shop ->
            val distance = distances[shop.id]
            val rating = ratings[shop.id] ?: Rating.ZERO
            shop.toListItem(distance, rating)
        }.sortedBy(ShopListViewItem::distance)
    }

    val state: StateFlow<ShopsListViewState> = shopsUiData.map {
        ShopsListViewState.Success(shops = it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ShopsListViewState.Loading
    )

    private val _effects = MutableSharedFlow<ShopsListViewEffect>(extraBufferCapacity = 1)
    val effects: SharedFlow<ShopsListViewEffect> = _effects

    fun openShopDetail(shopId: ShopId) = viewModelScope.launch {
        _effects.emit(ShopsListViewEffect.OpenShopDetail(shopId))
    }

    fun onBackClicked() = viewModelScope.launch {
        _effects.emit(ShopsListViewEffect.OnBackClicked)
    }
}

sealed interface ShopsListViewEffect {
    data class OpenShopDetail(val shopId: ShopId) : ShopsListViewEffect

    data object OnBackClicked : ShopsListViewEffect

    data class ShowError(val message: String) : ShopsListViewEffect
}