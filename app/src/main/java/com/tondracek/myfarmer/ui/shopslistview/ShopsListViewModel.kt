package com.tondracek.myfarmer.ui.shopslistview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.core.usecaseresult.getOrElse
import com.tondracek.myfarmer.location.usecase.MeasureDistanceFromMeUC
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ShopsListViewModel @Inject constructor(
    measureDistanceFromMe: MeasureDistanceFromMeUC,
    getAllShops: GetAllShopsUC,
    private val navigator: AppNavigator,
) : ViewModel() {

    private val _shops: Flow<UCResult<List<Shop>>> = getAllShops()

    val state: StateFlow<ShopsListViewState> = _shops.map { shopsResult ->
        val shops = shopsResult.getOrElse { return@map ShopsListViewState.Error(it) }

        val shopListItems = shops.map {
            val distance = measureDistanceFromMe(it.location.toLatLng())
            it.toListItem(distance)
        }

        ShopsListViewState.Success(shops = shopListItems)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = ShopsListViewState.Loading
    )

    fun navigateToShopDetail(shopId: ShopId) =
        navigator.navigate(Route.ShopDetailRoute(shopId.toString()))
}