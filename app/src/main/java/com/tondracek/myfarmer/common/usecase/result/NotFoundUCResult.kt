package com.tondracek.myfarmer.common.usecase.result

import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult

class NotFoundUCResult : UCResult.Failure(
    userError = "The requested item was not found.",
    systemError = "Item not found in the repository."
)