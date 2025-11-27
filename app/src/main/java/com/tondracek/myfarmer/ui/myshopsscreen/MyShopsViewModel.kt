package com.tondracek.myfarmer.ui.myshopsscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.core.usecaseresult.mapFlowUCSuccess
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.usecase.DeleteShopUC
import com.tondracek.myfarmer.shop.domain.usecase.GetShopsByUserUC
import com.tondracek.myfarmer.ui.core.navigation.AppNavigator
import com.tondracek.myfarmer.ui.core.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyShopsViewModel @Inject constructor(
    getShopsByUser: GetShopsByUserUC,
    private val deleteShopUC: DeleteShopUC,
    private val appNavigator: AppNavigator
) : ViewModel() {

    val state: StateFlow<MyShopsState> = getShopsByUser()
        .mapFlowUCSuccess { list ->
            list.map { it.toMyShopsListItem() }
        }
        .map {
            when (it) {
                is UCResult.Success -> MyShopsState.Success(shops = it.data)
                is UCResult.Failure -> MyShopsState.Error(failure = it)
            }
        }
        .stateIn(
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
        appNavigator.navigate(Route.CreateShopRoute)

    fun navigateToUpdateShop(shopId: ShopId) =
        appNavigator.navigate(Route.UpdateShopRoute(shopId.toString()))
}
