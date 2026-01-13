package com.tondracek.myfarmer.ui.myshopsscreen

import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.core.domain.domainerror.DomainError
import com.tondracek.myfarmer.core.domain.usecaseresult.getOrElse
import com.tondracek.myfarmer.core.domain.usecaseresult.withFailure
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.usecase.GetAverageRatingsByShopUC
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.usecase.DeleteShopUC
import com.tondracek.myfarmer.shop.domain.usecase.GetShopsByUserUC
import com.tondracek.myfarmer.ui.core.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyShopsViewModel @Inject constructor(
    getShopsByUser: GetShopsByUserUC,
    getAverageRatingsByShopUC: GetAverageRatingsByShopUC,
    private val deleteShopUC: DeleteShopUC,
) : BaseViewModel<MyShopsEffect>() {

    private val userShops: Flow<List<Shop>> = getShopsByUser()
        .withFailure { emitEffect(MyShopsEffect.ShowError(it.error)) }
        .getOrElse(emptyList())

    private val averageRatings: Flow<Map<ShopId, Rating>> = getAverageRatingsByShopUC()
        .withFailure { emitEffect(MyShopsEffect.ShowError(it.error)) }
        .getOrElse(emptyMap())

    val state: StateFlow<MyShopsState> = combine(
        userShops,
        averageRatings,
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

    fun navigateBack() = viewModelScope.launch {
        emitEffect(MyShopsEffect.OnGoBack)
    }

    fun navigateToShopDetail(shopId: ShopId) = viewModelScope.launch {
        emitEffect(MyShopsEffect.OpenShopDetail(shopId))
    }

    fun navigateToCreateShop() = viewModelScope.launch {
        emitEffect(MyShopsEffect.OpenCreateShop)
    }

    fun navigateToUpdateShop(shopId: ShopId) = viewModelScope.launch {
        emitEffect(MyShopsEffect.OpenUpdateShop(shopId))
    }
}

sealed interface MyShopsEffect {

    data class ShowError(val error: DomainError) : MyShopsEffect

    data object OnGoBack : MyShopsEffect

    data class OpenShopDetail(val shopId: ShopId) : MyShopsEffect

    data object OpenCreateShop : MyShopsEffect

    data class OpenUpdateShop(val shopId: ShopId) : MyShopsEffect
}