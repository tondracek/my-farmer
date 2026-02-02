package com.tondracek.myfarmer.review.domain.usecase

import com.tondracek.myfarmer.core.domain.domainresult.DomainResult
import com.tondracek.myfarmer.core.domain.domainresult.flatMap
import com.tondracek.myfarmer.core.domain.domainresult.getOrReturn
import com.tondracek.myfarmer.core.domain.domainresult.mapSuccess
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.review.domain.repository.ReviewPageCursor
import com.tondracek.myfarmer.review.domain.repository.ReviewRepository
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.user.domain.model.SystemUser
import com.tondracek.myfarmer.user.domain.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetShopReviewsWithAuthorsUC @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(shopId: ShopId): Flow<DomainResult<List<Pair<Review, SystemUser>>>> {
        val reviews = reviewRepository.getShopReviews(shopId)
        val authors = reviews.flatMap { reviewList ->
            val authorIds = reviewList.map { it.userId }.distinct()
            userRepository.getByIds(authorIds)
        }

        return combine(
            reviews,
            authors,
        ) { reviewResult, authorResult ->
            val reviewList = reviewResult.getOrReturn { return@combine it }
            val authorList = authorResult.getOrReturn { return@combine it }

            val authorsById = authorList.associateBy { it.id }
            reviewList
                .mapNotNull { review ->
                    val author = authorsById[review.userId]
                    author?.let { review to author }
                }
                .let { DomainResult.Success(it) }
        }
    }

    suspend fun paged(
        shopId: ShopId,
        limit: Int,
        after: ReviewPageCursor?
    ): DomainResult<Pair<List<Pair<Review, SystemUser>>, ReviewPageCursor?>> {
        val (reviews, nextCursor) = reviewRepository.getShopReviewsPaged(
            shopId = shopId,
            limit = limit,
            after = after
        ).getOrReturn { return it }

        if (reviews.isEmpty())
            return DomainResult.Success(emptyList<Pair<Review, SystemUser>>() to nextCursor)

        val authorIds = reviews.map { it.userId }.distinct()
        val authors: DomainResult<List<SystemUser>> = userRepository
            .getByIds(authorIds)
            .first()
        return authors.mapSuccess { list ->
            val authors = list.associateBy { it.id }

            reviews.mapNotNull { review ->
                authors[review.userId]?.let { review to it }
            } to nextCursor
        }
    }
}
