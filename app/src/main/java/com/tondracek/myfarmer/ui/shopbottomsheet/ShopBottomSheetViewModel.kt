package com.tondracek.myfarmer.ui.shopbottomsheet

import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.core.domain.domainerror.DomainError
import com.tondracek.myfarmer.core.domain.usecaseresult.getOrElse
import com.tondracek.myfarmer.core.domain.usecaseresult.withFailure
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.review.domain.usecase.GetReviewsPreviewUC
import com.tondracek.myfarmer.review.domain.usecase.GetShopAverageRatingUC
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.usecase.GetShopByIdUC
import com.tondracek.myfarmer.ui.common.review.ReviewUiState
import com.tondracek.myfarmer.ui.common.review.toUiState
import com.tondracek.myfarmer.ui.core.viewmodel.BaseViewModel
import com.tondracek.myfarmer.ui.shopdetailscreen.ShopDetailState
import com.tondracek.myfarmer.ui.shopdetailscreen.toShopDetailState
import com.tondracek.myfarmer.user.domain.model.SystemUser
import com.tondracek.myfarmer.user.domain.usecase.GetUserByIdUC
import com.tondracek.myfarmer.user.domain.usecase.GetUsersByIdsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ShopBottomSheetViewModel @Inject constructor(
    getShopById: GetShopByIdUC,
    getUserById: GetUserByIdUC,
    getUsersByIds: GetUsersByIdsUC,
    getReviewsPreview: GetReviewsPreviewUC,
    getShopAverageRating: GetShopAverageRatingUC,
) : BaseViewModel<ShopBottomSheetEffect>() {

    private val _openedShopId = MutableStateFlow<ShopId?>(null)
    val openedShopId: StateFlow<ShopId?> = _openedShopId

    fun openShop(id: ShopId?) = _openedShopId.update { id }
    fun closeShop() = openShop(null)

    private val selectedShop: Flow<Shop?> = _openedShopId.flatMapLatest { shopId ->
        when (shopId) {
            null -> flowOf(null)
            else -> getShopById(shopId)
                .withFailure { emitEffect(ShopBottomSheetEffect.EmitError(it.error)) }
                .getOrElse(null)
        }
    }

    private val shopSource: Flow<Shop> = selectedShop.filterNotNull()

    private val owner: Flow<SystemUser> =
        shopSource.flatMapLatest { getUserById(it.ownerId) }
            .withFailure { emitEffect(ShopBottomSheetEffect.EmitError(it.error)) }
            .mapNotNull { it.getOrNull() }

    private val reviewsPreview: Flow<List<Review>> =
        shopSource.flatMapLatest { getReviewsPreview(it.id) }
            .withFailure { emitEffect(ShopBottomSheetEffect.EmitError(it.error)) }
            .getOrElse(emptyList())

    private val reviewAuthors: Flow<List<SystemUser>> =
        reviewsPreview.flatMapLatest { reviews ->
            val authorIds = reviews.map { it.userId }.distinct()
            getUsersByIds(authorIds)
        }.withFailure { emitEffect(ShopBottomSheetEffect.EmitError(it.error)) }
            .getOrElse(emptyList())

    private val averageRating: Flow<Rating> =
        shopSource.flatMapLatest { getShopAverageRating(it.id) }
            .withFailure { emitEffect(ShopBottomSheetEffect.EmitError(it.error)) }
            .getOrElse(Rating.ZERO)

    private val reviewUiStates: Flow<List<ReviewUiState>> = combine(
        reviewsPreview,
        reviewAuthors,
    ) { reviews, authors ->
        val authors = authors.associateBy { it.id }
        reviews.mapNotNull { review ->
            authors[review.userId]
                ?.let { author -> review.toUiState(author = author) }
        }
    }

    val state: StateFlow<ShopDetailState> = combine(
        selectedShop,
        owner,
        reviewUiStates,
        averageRating,
    ) { shop, owner, reviewUiStates, averageRating ->
        when (shop) {
            null -> ShopDetailState.Loading
            else -> shop.toShopDetailState(owner, reviewUiStates, averageRating)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ShopDetailState.Loading
    )

    fun navigateToReviews() = viewModelScope.launch {
        val shopId = _openedShopId.value ?: return@launch
        emitEffect(ShopBottomSheetEffect.NavigateToReviews(shopId))
    }
}

sealed interface ShopBottomSheetEffect {

    data class EmitError(val error: DomainError) : ShopBottomSheetEffect

    data class NavigateToReviews(val shopId: ShopId) : ShopBottomSheetEffect
}