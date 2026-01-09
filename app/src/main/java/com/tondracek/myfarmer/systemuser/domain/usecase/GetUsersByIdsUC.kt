package com.tondracek.myfarmer.systemuser.domain.usecase

import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.core.usecaseresult.toUCResult
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import com.tondracek.myfarmer.systemuser.domain.model.UserId
import com.tondracek.myfarmer.systemuser.domain.port.GetUsersByIdsPort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetUsersByIdsUC @Inject constructor(
    private val getUsersByIdsPort: GetUsersByIdsPort,
) {

    operator fun invoke(ids: List<UserId>): Flow<UCResult<List<SystemUser>>> {
        if (ids.isEmpty())
            return flowOf(UCResult.Success(emptyList()))

        return getUsersByIdsPort(ids).toUCResult()
    }
}