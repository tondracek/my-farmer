package com.tondracek.myfarmer.ui.myshopsscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.core.usecaseresult.getOrElse
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.usecase.GetAverageRatingsByShopUC
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.usecase.DeleteShopUC
import com.tondracek.myfarmer.shop.domain.usecase.GetShopsByUserUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
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
) : ViewModel() {

    private val userShops: Flow<List<Shop>> = getShopsByUser()
        .getOrElse(emptyList()) { _effects.emit(MyShopsEffect.ShowError(it.userError)) }

    private val averageRatings: Flow<Map<ShopId, Rating>> = getAverageRatingsByShopUC()
        .getOrElse(emptyMap()) { _effects.emit(MyShopsEffect.ShowError(it.userError)) }

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

    private val _effects = MutableSharedFlow<MyShopsEffect>(extraBufferCapacity = 1)
    val effects: SharedFlow<MyShopsEffect> = _effects

    fun navigateBack() = viewModelScope.launch {
        _effects.emit(MyShopsEffect.OnGoBack)
    }

    fun navigateToShopDetail(shopId: ShopId) = viewModelScope.launch {
        _effects.emit(MyShopsEffect.OpenShopDetail(shopId))
    }

    fun navigateToCreateShop() = viewModelScope.launch {
        _effects.emit(MyShopsEffect.OpenCreateShop)
    }

    fun navigateToUpdateShop(shopId: ShopId) = viewModelScope.launch {
        _effects.emit(MyShopsEffect.OpenUpdateShop(shopId))
    }
}

sealed interface MyShopsEffect {
    data class ShowError(val message: String) : MyShopsEffect

    data object OnGoBack : MyShopsEffect

    data class OpenShopDetail(val shopId: ShopId) : MyShopsEffect

    data object OpenCreateShop : MyShopsEffect

    data class OpenUpdateShop(val shopId: ShopId) : MyShopsEffect
}