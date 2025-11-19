package com.tondracek.myfarmer.ui.shopdetailscreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.common.usecase.GetByIdUC
import com.tondracek.myfarmer.common.usecase.GetByIdsUC
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.core.usecaseresult.combineUCResults
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.review.domain.usecase.GetReviewsPreviewUC
import com.tondracek.myfarmer.review.domain.usecase.GetShopAverageRatingUC
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import com.tondracek.myfarmer.ui.common.review.toUiState
import com.tondracek.myfarmer.ui.core.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ShopDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getShopById: GetByIdUC<Shop>,
    getUserById: GetByIdUC<SystemUser>,
    getUsersByIds: GetByIdsUC<SystemUser>,
    getReviewsPreview: GetReviewsPreviewUC,
    getShopAverageRating: GetShopAverageRatingUC,
    private val navigator: AppNavigator
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

    fun navigateToReviews() {
        TODO()
    }

    fun navigateBack() = navigator.navigateBack()
}