package com.tondracek.myfarmer.review.domain.usecase

import com.tondracek.myfarmer.auth.domain.usecase.GetLoggedInUserUC
import com.tondracek.myfarmer.core.domain.domainerror.ReviewError
import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult
import com.tondracek.myfarmer.core.domain.usecaseresult.getOrReturn
import com.tondracek.myfarmer.core.domain.usecaseresult.mapSuccess
import com.tondracek.myfarmer.review.domain.model.ReviewInput
import com.tondracek.myfarmer.review.domain.model.toReview
import com.tondracek.myfarmer.review.domain.repository.ReviewRepository
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.repository.ShopRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CreateShopReviewUC @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val shopRepository: ShopRepository,
    private val getLoggedInUserUC: GetLoggedInUserUC,
) {

    suspend operator fun invoke(
        shopId: ShopId,
        reviewInput: ReviewInput,
    ): UCResult<Unit> {
        val user = getLoggedInUserUC().first().getOrReturn { return it }

        val shop = shopRepository.getById(shopId).first().getOrReturn { return it }
        if (shop.ownerId == user.id)
            return UCResult.Failure(ReviewError.CreatingToOwnShopNotAllowed)

        val review = reviewInput.toReview(shopId, user.id)
        return reviewRepository.create(review).mapSuccess { }
    }
}

