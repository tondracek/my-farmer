package com.tondracek.myfarmer.ui.reviewscreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import androidx.paging.map
import com.tondracek.myfarmer.auth.domain.usecase.GetLoggedInUserUC
import com.tondracek.myfarmer.core.domain.domainerror.DomainError
import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import com.tondracek.myfarmer.core.domain.usecaseresult.flatMap
import com.tondracek.myfarmer.core.domain.usecaseresult.getOrElse
import com.tondracek.myfarmer.core.domain.usecaseresult.mapSuccess
import com.tondracek.myfarmer.core.domain.usecaseresult.withFailure
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.review.domain.model.ReviewId
import com.tondracek.myfarmer.review.domain.model.ReviewInput
import com.tondracek.myfarmer.review.domain.repository.ReviewPageCursor
import com.tondracek.myfarmer.review.domain.usecase.CreateShopReviewUC
import com.tondracek.myfarmer.review.domain.usecase.DeleteReviewUC
import com.tondracek.myfarmer.review.domain.usecase.GetShopReviewsWithAuthorsUC
import com.tondracek.myfarmer.review.domain.usecase.GetUserReviewOnShopUC
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.usecase.GetShopByIdUC
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import com.tondracek.myfarmer.ui.common.review.ReviewUiState
import com.tondracek.myfarmer.ui.common.review.toUiState
import com.tondracek.myfarmer.ui.core.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopReviewsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getShopById: GetShopByIdUC,
    getUserReviewOnShopUC: GetUserReviewOnShopUC,
    getLoggedInUserUC: GetLoggedInUserUC,
    private val deleteReviewUC: DeleteReviewUC,
    private val getShopReviewsWithAuthorsUC: GetShopReviewsWithAuthorsUC,
    private val createShopReview: CreateShopReviewUC,
) : BaseViewModel<ShopReviewsEffect>() {

    private val currentUser: Flow<DomainResult<SystemUser>> = getLoggedInUserUC()

    private val shopId: ShopId = savedStateHandle.getReviewsScreenShopId()

    private val shop: Flow<Shop?> = getShopById(shopId)
        .withFailure { emitEffect(ShopReviewsEffect.ShowError(it.error)) }
        .map { it.getOrNull() }

    private val myReview: Flow<Review?> = currentUser
        .flatMap { getUserReviewOnShopUC(shopId = shopId, userId = it.id) }
        .map { it.getOrNull() }

    private val isMyShop: Flow<Boolean> = combine(shop, currentUser) { shop, userResult ->
        userResult.mapSuccess { shop?.ownerId == it.id }
            .getOrElse(false)
    }

    private val isLoggedIn: Flow<Boolean> = currentUser.map { it.isSuccess() }

    private val myReviewUiState: Flow<ReviewUiState?> = combine(
        myReview,
        currentUser,
    ) { review, userResult ->
        val author = userResult.getOrNull() ?: return@combine null
        when (review) {
            null -> null
            else -> review.toUiState(author)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _reviewsWithAuthors: Flow<PagingData<Pair<Review, SystemUser>>> =
        myReview.flatMapLatest {
            getReviewsPageFlow(
                shopId = shopId,
                showError = { emitEffect(ShopReviewsEffect.ShowError(it)) },
                getReviewsPaged = { shopId, limit, after ->
                    getShopReviewsWithAuthorsUC.paged(
                        shopId = shopId,
                        limit = limit,
                        after = after
                    )
                }
            )
        }.cachedIn(viewModelScope)

    private val _reviewsUiState: Flow<PagingData<ReviewUiState>> =
        _reviewsWithAuthors.map { pagingData ->
            pagingData.map { (review, author) -> review.toUiState(author) }
        }

    val state: StateFlow<ShopReviewsScreenState> = combine(
        shop,
        isMyShop,
        myReviewUiState,
        isLoggedIn,
        flowOf(_reviewsUiState)
    ) { shop, isMyShop, myReview, isLoggedIn, reviews ->
        ShopReviewsScreenState.Success(
            shopName = shop?.name,
            isMyShop = isMyShop,
            myReview = myReview,
            isLoggedIn = isLoggedIn,
            reviews = reviews,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ShopReviewsScreenState.Loading
    )

    fun onSubmitReview(reviewInput: ReviewInput) = viewModelScope.launch {
        createShopReview(
            shopId = shopId,
            reviewInput = reviewInput
        ).withFailure { emitEffect(ShopReviewsEffect.ShowError(it.error)) }
    }

    fun onReviewDeleteClick(reviewId: ReviewId) = viewModelScope.launch {
        deleteReviewUC(reviewId)
            .withFailure { emitEffect(ShopReviewsEffect.ShowError(it.error)) }
    }
}

sealed interface ShopReviewsEffect {
    data class ShowError(val error: DomainError) : ShopReviewsEffect
}

fun getReviewsPageFlow(
    shopId: ShopId,
    showError: suspend (error: DomainError) -> Unit,
    getReviewsPaged: suspend (shopId: ShopId, pageSize: Int, cursor: ReviewPageCursor?) -> DomainResult<Pair<List<Pair<Review, SystemUser>>, ReviewPageCursor?>>,
): Flow<PagingData<Pair<Review, SystemUser>>> = Pager(
    config = PagingConfig(
        pageSize = 20,
        enablePlaceholders = false
    ),
    pagingSourceFactory = {
        ReviewsPagingSource(
            shopId = shopId,
            showError = showError,
            getReviewsPaged = getReviewsPaged
        )
    }
).flow

private class ReviewsPagingSource(
    private val shopId: ShopId,
    private val showError: suspend (error: DomainError) -> Unit,
    private val getReviewsPaged: suspend (shopId: ShopId, pageSize: Int, cursor: ReviewPageCursor?) -> DomainResult<Pair<List<Pair<Review, SystemUser>>, ReviewPageCursor?>>,
) : PagingSource<ReviewPageCursor, Pair<Review, SystemUser>>() {

    override suspend fun load(params: LoadParams<ReviewPageCursor>): LoadResult<ReviewPageCursor, Pair<Review, SystemUser>> {
        val cursor = params.key

        val result: DomainResult<Pair<List<Pair<Review, SystemUser>>, ReviewPageCursor?>> =
            getReviewsPaged(shopId, params.loadSize, cursor)

        return when (result) {
            is DomainResult.Success<Pair<List<Pair<Review, SystemUser>>, ReviewPageCursor?>> -> {
                val (reviews, nextCursor) = result.data
                LoadResult.Page(
                    data = reviews,
                    prevKey = null,
                    nextKey = nextCursor,
                )
            }

            is DomainResult.Failure -> result
                .withFailure { showError(it.error) }
                .let { LoadResult.Error(result.cause ?: Exception()) }
        }
    }

    override fun getRefreshKey(state: PagingState<ReviewPageCursor, Pair<Review, SystemUser>>): ReviewPageCursor? {
        return null
    }
}