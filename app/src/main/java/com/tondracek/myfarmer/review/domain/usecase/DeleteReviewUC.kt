package com.tondracek.myfarmer.review.domain.usecase

import com.tondracek.myfarmer.auth.domain.usecase.GetLoggedInUserUC
import com.tondracek.myfarmer.core.domain.domainerror.ReviewError
import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import com.tondracek.myfarmer.core.domain.usecaseresult.getOrReturn
import com.tondracek.myfarmer.core.domain.usecaseresult.mapSuccess
import com.tondracek.myfarmer.review.domain.model.ReviewId
import com.tondracek.myfarmer.review.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DeleteReviewUC @Inject constructor(
    private val getLoggedInUserUC: GetLoggedInUserUC,
    private val reviewRepository: ReviewRepository,
) {

    suspend operator fun invoke(reviewId: ReviewId): DomainResult<Unit> {
        val loggedUser = getLoggedInUserUC().first()
            .getOrReturn { return it }

        val isAuthor = reviewRepository.getById(reviewId).first()
            .mapSuccess { review -> review.userId == loggedUser.id }
            .getOrReturn { return it }

        if (!isAuthor)
            return DomainResult.Failure(ReviewError.NotAuthor)

        return reviewRepository.delete(reviewId)
    }
}