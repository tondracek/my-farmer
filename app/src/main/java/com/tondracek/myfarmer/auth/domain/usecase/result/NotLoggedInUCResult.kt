package com.tondracek.myfarmer.auth.domain.usecase.result

import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult

class NotLoggedInUCResult : UCResult.Failure(
    userError = "You must be logged in to perform this action.",
    systemError = "User is not logged in."
)
