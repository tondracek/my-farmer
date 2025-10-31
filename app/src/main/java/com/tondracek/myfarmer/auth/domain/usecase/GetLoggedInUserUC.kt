package com.tondracek.myfarmer.auth.domain.usecase

import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.systemuser.data.user0
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetLoggedInUserUC @Inject constructor(
) : () -> Flow<UCResult<SystemUser>> {

    override operator fun invoke(): Flow<UCResult<SystemUser>> = flowOf(
        UCResult.Success(user0)
    )
}