package com.tondracek.myfarmer.ui.shopdetailscreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.core.usecaseresult.combineUCResults
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.review.domain.usecase.GetReviewsPreviewUC
import com.tondracek.myfarmer.review.domain.usecase.GetShopAverageRatingUC
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.usecase.GetShopByIdUC
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import com.tondracek.myfarmer.systemuser.domain.usecase.GetUserByIdUC
import com.tondracek.myfarmer.systemuser.domain.usecase.GetUsersByIdsUC
import com.tondracek.myfarmer.ui.common.review.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ShopDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getShopById: GetShopByIdUC,
    getUserById: GetUserByIdUC,
    getUsersByIds: GetUsersByIdsUC,
    getReviewsPreview: GetReviewsPreviewUC,
    getShopAverageRating: GetShopAverageRatingUC,
) : ViewModel() {

    private val shopId: ShopId = savedStateHandle.getShopDetailScreenShopId()

    private val shop: Flow<UCResult<Shop>> = getShopById(shopId)

    private val owner: Flow<UCResult<SystemUser>> =
        shop.mapNotNull { it.getOrNull() }
            .flatMapLatest { getUserById(it.ownerId) }

    private val reviewsPreview: Flow<UCResult<List<Review>>> = getReviewsPreview(shopId)

    private val reviewAuthors: Flow<UCResult<List<SystemUser>>> =
        reviewsPreview.mapNotNull { it.getOrNull() }
            .flatMapLatest { reviews ->
                val authorIds = reviews.map { it.userId }.distinct()
                getUsersByIds(authorIds)
            }

    private val averageRating: Flow<UCResult<Rating>> = getShopAverageRating(shopId)

    val state: StateFlow<ShopDetailState> = combineUCResults(
        shop,
        owner,
        reviewsPreview,
        reviewAuthors,
        averageRating,
        { failure -> ShopDetailState.Error(failure) }
    ) { shop, owner, reviewsPreview, reviewsAuthors, averageRating ->
        val reviewUiStates = reviewsPreview.mapNotNull { review ->
            val author = reviewsAuthors.find { it.id == review.userId } ?: return@mapNotNull null
            review.toUiState(author = author)
        }
        shop.toShopDetailState(owner, reviewUiStates, averageRating)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5_000),
        initialValue = ShopDetailState.Loading
    )

    private val _effects = MutableSharedFlow<ShopDetailEffect>(extraBufferCapacity = 1)
    val effects: SharedFlow<ShopDetailEffect> = _effects

    fun navigateToReviews() = viewModelScope.launch {
        _effects.emit(ShopDetailEffect.NavigateToReviews(shopId))
    }

    fun navigateBack() = viewModelScope.launch {
        _effects.emit(ShopDetailEffect.NavigateBack)
    }
}

sealed interface ShopDetailEffect {
    object NavigateBack : ShopDetailEffect
    data class NavigateToReviews(val shopId: ShopId) : ShopDetailEffect
}