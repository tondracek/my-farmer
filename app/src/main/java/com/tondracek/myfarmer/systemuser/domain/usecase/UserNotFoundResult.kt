package com.tondracek.myfarmer.systemuser.domain.usecase

import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult
import com.tondracek.myfarmer.systemuser.domain.model.UserId

data class UserNotFoundResult(private val id: UserId) : UCResult.Failure(
    "User not found",
    "User with id $id was not found in the database."
)
