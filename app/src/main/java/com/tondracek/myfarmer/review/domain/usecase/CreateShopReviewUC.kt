package com.tondracek.myfarmer.review.domain.usecase

import com.tondracek.myfarmer.auth.domain.usecase.GetLoggedInUserUC
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.core.usecaseresult.getOrReturn
import com.tondracek.myfarmer.review.data.ReviewRepository
import com.tondracek.myfarmer.review.domain.model.ReviewInput
import com.tondracek.myfarmer.review.domain.model.toReview
import com.tondracek.myfarmer.shop.domain.model.ShopId
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CreateShopReviewUC @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val getLoggedInUserUC: GetLoggedInUserUC,
) {

    suspend operator fun invoke(
        shopId: ShopId,
        reviewInput: ReviewInput,
    ): UCResult<Unit> = UCResult.of("Failed to create shop review.") {
        val user = getLoggedInUserUC().first().getOrReturn { return it }

        val review = reviewInput.toReview(shopId, user.id)
        reviewRepository.create(review)
    }
}

