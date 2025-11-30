package com.tondracek.myfarmer.review.domain.usecase

import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.core.usecaseresult.toUCResult
import com.tondracek.myfarmer.review.data.ReviewRepository
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.review.domain.usecase.result.UCFailureLoadingReviews
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.systemuser.data.UserRepository
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class GetShopReviewsWithAuthorsUC @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(shopId: ShopId): Flow<UCResult<List<Pair<Review, SystemUser>>>> {
        val reviews: Flow<List<Review>> = reviewRepository.getReviews(shopId)
        val authors: Flow<List<SystemUser>> = reviews.flatMapLatest { reviewList ->
            val authorIds = reviewList.map { it.userId }.distinct()
            println(authorIds)
            userRepository.getByIds(authorIds)
        }

        return combine(reviews, authors) { reviewList, authorList ->
            val authorsById = authorList.associateBy { it.id }
            reviewList.mapNotNull { review ->
                val author = authorsById[review.userId]
                author?.let { review to author }
            }
        }.toUCResult(UCFailureLoadingReviews())
    }
}