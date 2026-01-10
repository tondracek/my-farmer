package com.tondracek.myfarmer.ui.mainshopscreen.shopslistview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.tondracek.myfarmer.core.usecaseresult.getOrElse
import com.tondracek.myfarmer.location.usecase.MeasureDistanceFromMeUC
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.usecase.GetAverageRatingsByShopUC
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.paging.ShopsPagingSource
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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
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

    val averageRatings: Flow<Map<ShopId, Rating>> =
        getAverageRatingsByShopUC()
            .getOrElse(emptyMap()) {
                _effects.emit(ShopsListViewEffect.ShowError(it.userError))
            }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _shopsPagingData: Flow<PagingData<Shop>> =
        filters.flatMapLatest { filtersValue ->
            Pager(
                config = PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = {
                    ShopsPagingSource(
                        getData = { limit, after ->
                            getAllShops.paged(
                                limit = limit,
                                after = after,
                                filters = filtersValue
                            ).getOrElse(emptyList()) {
                                _effects.emit(
                                    ShopsListViewEffect.ShowError(it.userError)
                                )
                            }
                        }
                    )
                }
            ).flow
        }.cachedIn(viewModelScope)

    private val _shopsUiPagingData: Flow<PagingData<ShopListViewItem>> =
        combine(_shopsPagingData, averageRatings) { pagingData, ratings ->
            pagingData.map { shop ->
                val distance = measureDistanceFromMe(shop.location)
                val rating = ratings[shop.id] ?: Rating.ZERO
                shop.toListItem(distance, rating)
            }
        }

    val state: StateFlow<ShopsListViewState> = flowOf(
        ShopsListViewState.Success(shops = _shopsUiPagingData)
    ).stateIn(
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