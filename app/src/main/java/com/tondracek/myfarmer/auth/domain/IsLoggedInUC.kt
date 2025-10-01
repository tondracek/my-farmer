package com.tondracek.myfarmer.auth.domain

import com.tondracek.myfarmer.core.domain.UseCaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.UUID
import javax.inject.Inject

typealias UserId = UUID

data class SystemUser(
    val id: UserId
)

class IsLoggedInUC @Inject constructor(
) : () -> Flow<UseCaseResult<SystemUser>> {

    override operator fun invoke(): Flow<UseCaseResult<SystemUser>> = flowOf(
        UseCaseResult.Success(SystemUser(UUID.randomUUID()))
    )
}