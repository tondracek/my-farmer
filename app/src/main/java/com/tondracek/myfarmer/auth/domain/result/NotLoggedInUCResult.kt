package com.tondracek.myfarmer.auth.domain.result

import com.tondracek.myfarmer.core.usecaseresult.UseCaseResult

class NotLoggedInUCResult : UseCaseResult.Failure(
    userError = "You must be logged in to perform this action.",
    systemError = "User is not logged in."
)
