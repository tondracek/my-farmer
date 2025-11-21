package com.tondracek.myfarmer.systemuser.domain.usecase

import com.tondracek.myfarmer.common.usecase.result.NotFoundUCResult
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.systemuser.data.UserRepository
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class GetUserByIdUC @Inject constructor(
    private val repository: UserRepository,
) {

    operator fun invoke(id: UUID?): Flow<UCResult<SystemUser>> = when (id == null) {
        false -> repository.getById(id).map {
            when (it == null) {
                false -> UCResult.Success(it)
                true -> NotFoundUCResult()
            }
        }

        true -> flowOf(NotFoundUCResult())
    }
}

