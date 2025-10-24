package com.tondracek.myfarmer.auth.domain

import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.UUID
import javax.inject.Inject

class IsLoggedInUC @Inject constructor(
) : () -> Flow<UCResult<SystemUser>> {

    override operator fun invoke(): Flow<UCResult<SystemUser>> = flowOf(
        UCResult.Success(SystemUser(UUID.randomUUID()))
    )
}