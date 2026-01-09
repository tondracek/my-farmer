package com.tondracek.myfarmer.systemuser.domain.usecase

import com.tondracek.myfarmer.core.usecaseresult.UCResult
import java.util.UUID

data class UserNotFoundResult(private val id: UUID) : UCResult.Failure(
    "User not found",
    "User with id $id was not found in the database."
)
