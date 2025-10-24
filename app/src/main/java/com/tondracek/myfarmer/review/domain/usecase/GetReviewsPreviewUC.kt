package com.tondracek.myfarmer.review.domain.usecase

import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.shop.domain.model.ShopId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetReviewsPreviewUC @Inject constructor() {

    operator fun invoke(shopId: ShopId): Flow<UCResult<List<Review>>> =
        flowOf(UCResult.Success(emptyList()))
}