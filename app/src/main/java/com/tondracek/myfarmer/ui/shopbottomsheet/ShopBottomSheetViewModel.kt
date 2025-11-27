package com.tondracek.myfarmer.ui.shopbottomsheet

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
import com.tondracek.myfarmer.ui.core.navigation.AppNavigator
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.shopdetailscreen.ShopDetailState
import com.tondracek.myfarmer.ui.shopdetailscreen.toShopDetailState
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
class ShopBottomSheetViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getShopById: GetShopByIdUC,
    getUserById: GetUserByIdUC,
    getUsersByIds: GetUsersByIdsUC,
    getReviewsPreview: GetReviewsPreviewUC,
    getShopAverageRating: GetShopAverageRatingUC,
    private val navigator: AppNavigator
) : ViewModel() {

    private val shopId: ShopId = savedStateHandle.getShopBottomSheetShopId()

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
    ) { shop, owner, reviews, reviewsAuthors, averageRating ->
        val reviewUiStates = reviews.mapNotNull { review ->
            val author = reviewsAuthors.find { it.id == review.userId } ?: return@mapNotNull null
            review.toUiState(author = author)
        }
        shop.toShopDetailState(owner, reviewUiStates, averageRating)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ShopDetailState.Loading
    )

    fun navigateToReviews() = navigator.navigate(Route.ShopReviews(shopId.toString()))

    fun navigateBack() = navigator.navigateBack()
}