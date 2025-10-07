package com.tondracek.myfarmer.auth.domain

import com.tondracek.myfarmer.core.usecaseresult.UseCaseResult
import com.tondracek.myfarmer.systemuser.SystemUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.UUID
import javax.inject.Inject

class IsLoggedInUC @Inject constructor(
) : () -> Flow<UseCaseResult<SystemUser>> {

    override operator fun invoke(): Flow<UseCaseResult<SystemUser>> = flowOf(
        UseCaseResult.Success(SystemUser(UUID.randomUUID()))
    )
}