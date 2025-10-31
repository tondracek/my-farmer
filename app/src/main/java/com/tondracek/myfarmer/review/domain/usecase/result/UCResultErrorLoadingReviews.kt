package com.tondracek.myfarmer.review.domain.usecase.result

import com.tondracek.myfarmer.core.usecaseresult.UCResult

class UCResultErrorLoadingReviews(throwable: Throwable) : UCResult.Failure(
    userError = "Couldn't load reviews",
    systemError = throwable.message,
)