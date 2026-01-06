package com.tondracek.myfarmer.ui.mainshopscreen.shopslistview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.core.usecaseresult.getOrElse
import com.tondracek.myfarmer.location.usecase.MeasureDistanceFromMeUC
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.usecase.GetAverageRatingsByShopUC
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.usecase.GetAllShopsUC
import com.tondracek.myfarmer.shopfilters.domain.model.ShopFilters
import com.tondracek.myfarmer.shopfilters.domain.usecase.GetShopFiltersUC
import com.tondracek.myfarmer.ui.common.shop.filter.ShopFiltersRepositoryKeys
import com.tondracek.myfarmer.ui.core.uiState.UiState
import com.tondracek.myfarmer.ui.core.uiState.asUiState
import com.tondracek.myfarmer.ui.core.uiState.getOrReturn
import com.tondracek.myfarmer.ui.mainshopscreen.shopslistview.components.ShopListViewItem
import com.tondracek.myfarmer.ui.mainshopscreen.shopslistview.components.toListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopsListViewModel @Inject constructor(
    getAverageRatingsByShopUC: GetAverageRatingsByShopUC,
    measureDistanceFromMe: MeasureDistanceFromMeUC,
    getAllShops: GetAllShopsUC,
    getShopFilters: GetShopFiltersUC,
) : ViewModel() {

    private val filters: StateFlow<ShopFilters> =
        getShopFilters(ShopFiltersRepositoryKeys.MAIN_SHOPS_SCREEN)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val shopsUiState: Flow<UiState<List<Shop>>> = filters
        .flatMapLatest { getAllShops(filters = it) }
        .asUiState(emptyList()) { _effects.emit(ShopsListViewEffect.ShowError(it.userError)) }

    val averageRatings: Flow<Map<ShopId, Rating>> = getAverageRatingsByShopUC()
        .getOrElse(emptyMap()) { _effects.emit(ShopsListViewEffect.ShowError(it.userError)) }

    val state: StateFlow<ShopsListViewState> = combine(
        shopsUiState,
        averageRatings,
    ) { shopsUiState, ratings ->
        val shops = shopsUiState.getOrReturn { return@combine ShopsListViewState.Loading }

        val shopListItems = shops.map {
            val distance = measureDistanceFromMe(it.location)
            val rating = ratings[it.id] ?: Rating.ZERO
            it.toListItem(distance, rating)
        }.sortedBy(ShopListViewItem::distance)

        ShopsListViewState.Success(shops = shopListItems)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = ShopsListViewState.Loading
    )

    private val _effects = MutableSharedFlow<ShopsListViewEffect>(extraBufferCapacity = 1)
    val effects = _effects.asSharedFlow()

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