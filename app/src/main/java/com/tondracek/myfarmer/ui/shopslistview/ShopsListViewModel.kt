package com.tondracek.myfarmer.ui.shopslistview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.core.usecaseresult.combineUCResults
import com.tondracek.myfarmer.location.usecase.MeasureDistanceFromMeUC
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.usecase.GetAverageRatingsByShopUC
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.usecase.GetAllShopsUC
import com.tondracek.myfarmer.ui.core.navigation.AppNavigator
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.shopslistview.components.toListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ShopsListViewModel @Inject constructor(
    getAverageRatingsByShopUC: GetAverageRatingsByShopUC,
    measureDistanceFromMe: MeasureDistanceFromMeUC,
    getAllShops: GetAllShopsUC,
    private val navigator: AppNavigator,
) : ViewModel() {

    private val shops: Flow<UCResult<List<Shop>>> = getAllShops()

    private val averageRatings: Flow<UCResult<Map<ShopId, Rating>>> = getAverageRatingsByShopUC()

    val state: StateFlow<ShopsListViewState> = combineUCResults(
        shops,
        averageRatings,
        { ShopsListViewState.Error(it) }
    ) { shops, ratings ->
        val shopListItems = shops.map {
            val distance = measureDistanceFromMe(it.location)
            val rating = ratings[it.id] ?: Rating.ZERO
            it.toListItem(distance, rating)
        }

        ShopsListViewState.Success(shops = shopListItems)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = ShopsListViewState.Loading
    )

    fun navigateToShopDetail(shopId: ShopId) =
        navigator.navigate(Route.ShopDetailRoute(shopId.toString()))

    fun navigateBack() = navigator.navigateBack()
}