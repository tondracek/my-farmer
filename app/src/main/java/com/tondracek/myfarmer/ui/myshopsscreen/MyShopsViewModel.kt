package com.tondracek.myfarmer.ui.myshopsscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.core.usecaseresult.combineUCResults
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.usecase.GetAverageRatingsByShopUC
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.usecase.DeleteShopUC
import com.tondracek.myfarmer.shop.domain.usecase.GetShopsByUserUC
import com.tondracek.myfarmer.ui.core.navigation.AppNavigator
import com.tondracek.myfarmer.ui.core.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyShopsViewModel @Inject constructor(
    getShopsByUser: GetShopsByUserUC,
    getAverageRatingsByShopUC: GetAverageRatingsByShopUC,
    private val deleteShopUC: DeleteShopUC,
    private val appNavigator: AppNavigator
) : ViewModel() {

    private val userShops: Flow<UCResult<List<Shop>>> = getShopsByUser()

    private val averageRatings: Flow<UCResult<Map<ShopId, Rating>>> = getAverageRatingsByShopUC()

    val state: StateFlow<MyShopsState> = combineUCResults(
        userShops,
        averageRatings,
        { MyShopsState.Error(it) }
    ) { shops, ratings ->
        val shopUiItems = shops.map { shop ->
            val rating = ratings[shop.id] ?: Rating.ZERO
            shop.toMyShopsListItem(rating)
        }

        MyShopsState.Success(shops = shopUiItems)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MyShopsState.Loading
    )

    fun deleteShop(shopId: ShopId) = viewModelScope.launch {
        deleteShopUC(shopId)
    }

    fun navigateBack() = appNavigator.navigateBack()

    fun navigateToShopDetail(shopId: ShopId) =
        appNavigator.navigate(Route.ShopDetailRoute(shopId.toString()))

    fun navigateToCreateShop() =
        appNavigator.navigate(Route.CreateShop)

    fun navigateToUpdateShop(shopId: ShopId) =
        appNavigator.navigate(Route.UpdateShop(shopId.toString()))
}
