package com.tondracek.myfarmer.review.domain.paging

import com.tondracek.myfarmer.common.paging.IdPagingSource
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.review.domain.model.ReviewId
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser

class ShopReviewsPagingSource(
    getData: suspend (limit: Int, after: ReviewId?) -> List<Pair<Review, SystemUser>>
) : IdPagingSource<ReviewId, Pair<Review, SystemUser>>(
    getDataKey = { (review, user) -> review.id },
    getData = getData
)
