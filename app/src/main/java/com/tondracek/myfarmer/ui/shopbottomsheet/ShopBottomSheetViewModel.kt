package com.tondracek.myfarmer.ui.shopbottomsheet

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.common.usecase.GetByIdUC
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.core.usecaseresult.getOrReturn
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.review.domain.usecase.GetReviewsPreviewUC
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import com.tondracek.myfarmer.ui.common.layout.shopdetaillayout.ShopDetailState
import com.tondracek.myfarmer.ui.common.layout.shopdetaillayout.toShopDetailState
import com.tondracek.myfarmer.ui.core.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ShopBottomSheetViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getShopById: GetByIdUC<Shop>,
    getUserById: GetByIdUC<SystemUser>,
    getReviewsPreview: GetReviewsPreviewUC,
    private val navigator: AppNavigator
) : ViewModel() {

    private val shopId: Flow<ShopId> =
        flowOf(savedStateHandle.getShopBottomSheetShopId()) // TODO flow?

    @OptIn(ExperimentalCoroutinesApi::class)
    private val shop: Flow<UCResult<Shop>> =
        shopId.flatMapLatest { getShopById(it) }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val owner: Flow<UCResult<SystemUser>> =
        shop.mapNotNull { it.getOrNull() }
            .flatMapLatest { getUserById(it.ownerId) }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val reviewsPreview: Flow<UCResult<List<Review>>> =
        shopId.flatMapLatest { getReviewsPreview(it) }


    val state: StateFlow<ShopDetailState> = combine(
        shop,
        owner,
        reviewsPreview,
    ) { shopResult, ownerResult, reviewsResult ->
        val shop = shopResult.getOrReturn { return@combine ShopDetailState.Error(it) }
        val owner = ownerResult.getOrReturn { return@combine ShopDetailState.Error(it) }
        val reviews = reviewsResult.getOrReturn { return@combine ShopDetailState.Error(it) }

        shop.toShopDetailState(owner, reviews)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5_000),
        initialValue = ShopDetailState.Loading
    )

    fun navigateToReviews() {
        TODO()
    }

    fun navigateBack() = navigator.navigateBack()
}