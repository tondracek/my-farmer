package com.tondracek.myfarmer.review.domain.usecase

import com.tondracek.myfarmer.auth.domain.usecase.result.NotLoggedInUCResult
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.review.data.ReviewRepository
import com.tondracek.myfarmer.review.domain.model.ReviewInput
import com.tondracek.myfarmer.review.domain.model.toReview
import com.tondracek.myfarmer.systemuser.data.UserRepository
import kotlinx.coroutines.flow.first
import java.util.UUID
import javax.inject.Inject

class CreateShopReviewUC @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(
        shopId: UUID,
        reviewInput: ReviewInput,
    ): UCResult<Unit> = UCResult.of("Failed to create shop review.") {
        val user = userRepository.getLoggedInUser().first() ?: return NotLoggedInUCResult()
        val review = reviewInput.toReview(shopId, user.id)
        reviewRepository.create(review)
    }
}

