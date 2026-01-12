package com.tondracek.myfarmer.ui.reviewscreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.review.domain.model.ReviewId
import com.tondracek.myfarmer.review.domain.model.ReviewInput
import com.tondracek.myfarmer.review.domain.usecase.CreateShopReviewUC
import com.tondracek.myfarmer.review.domain.usecase.GetShopReviewsWithAuthorsUC
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.usecase.GetShopByIdUC
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import com.tondracek.myfarmer.ui.common.paging.getUCResultPageFlow
import com.tondracek.myfarmer.ui.common.review.ReviewUiState
import com.tondracek.myfarmer.ui.common.review.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopReviewsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getShopById: GetShopByIdUC,
    private val getShopReviewsWithAuthorsUC: GetShopReviewsWithAuthorsUC,
    private val createShopReview: CreateShopReviewUC,
) : ViewModel() {

    val shopId: ShopId = savedStateHandle.getReviewsScreenShopId()

    val shop: Flow<UCResult<Shop>> = getShopById(shopId)

    private val _reviewsWithAuthors: Flow<PagingData<Pair<Review, SystemUser>>> =
        getUCResultPageFlow<ReviewId, Pair<Review, SystemUser>>({ (review, _) -> review.id }) { limit, after ->
            getShopReviewsWithAuthorsUC.paged(
                shopId = shopId,
                limit = limit,
                after = after,
            )
        }.cachedIn(viewModelScope)

    private val _reviewsUiState: Flow<PagingData<ReviewUiState>> =
        _reviewsWithAuthors.map { pagingData ->
            pagingData.map { (review, author) -> review.toUiState(author) }
        }

    val state: StateFlow<ShopReviewsScreenState> = shop
        .map {
            it.fold(
                onSuccess = { shop ->
                    ShopReviewsScreenState.Success(
                        shopName = shop.name,
                        reviews = _reviewsUiState,
                    )
                },
                onFailure = { failure ->
                    ShopReviewsScreenState.Error(failure)
                }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5_000),
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

    data class ShowError(val message: String) : ShopReviewsEffect
}