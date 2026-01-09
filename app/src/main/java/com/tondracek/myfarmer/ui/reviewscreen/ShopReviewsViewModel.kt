package com.tondracek.myfarmer.ui.reviewscreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.core.usecaseresult.combineUCResults
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.review.domain.model.ReviewInput
import com.tondracek.myfarmer.review.domain.usecase.CreateShopReviewUC
import com.tondracek.myfarmer.review.domain.usecase.GetShopReviewsWithAuthorsUC
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.usecase.GetShopByIdUC
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import com.tondracek.myfarmer.ui.common.review.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ShopReviewsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getShopById: GetShopByIdUC,
    getShopReviewsWithAuthorsUC: GetShopReviewsWithAuthorsUC,
    private val createShopReview: CreateShopReviewUC,
) : ViewModel() {

    val shopId: UUID = savedStateHandle.getReviewsScreenShopId()

    val shop: Flow<UCResult<Shop>> = getShopById(shopId)
    val reviewsWithAuthors: Flow<UCResult<List<Pair<Review, SystemUser>>>> =
        getShopReviewsWithAuthorsUC(shopId)

    val state: StateFlow<ShopReviewsScreenState> = combineUCResults(
        reviewsWithAuthors,
        shop,
        { ShopReviewsScreenState.Error(it) }
    ) { reviews, shop ->
        ShopReviewsScreenState.Success(
            shopName = shop.name,
            reviews = reviews.map { (review, author) -> review.toUiState(author) }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ShopReviewsScreenState.Loading
    )

    private val _effects = MutableSharedFlow<ShopReviewsEffect>(extraBufferCapacity = 1)
    val effects: SharedFlow<ShopReviewsEffect> = _effects

    fun onSubmitReview(reviewInput: ReviewInput) = viewModelScope.launch {
        createShopReview(
            shopId = shopId,
            reviewInput = reviewInput
        )
    }

    fun onNavigateBack() = viewModelScope.launch {
        _effects.emit(ShopReviewsEffect.NavigateBack)
    }
}

sealed interface ShopReviewsEffect {
    object NavigateBack : ShopReviewsEffect
}