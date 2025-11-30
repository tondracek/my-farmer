package com.tondracek.myfarmer.review.domain.usecase.result

import com.tondracek.myfarmer.core.usecaseresult.UCResult

data object UCFailureCreatingShopReview : UCResult.Failure(
    userError = "Failed to create shop review.",
    systemError = "Failed to create shop review.",
)