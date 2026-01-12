package com.tondracek.myfarmer.core.domain.usecaseresult

data object ForbiddenUCResult : UCResult.Failure(
    userError = "You do not have permission to perform this action",
)
