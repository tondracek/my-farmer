package com.tondracek.myfarmer.user.domain.usecase

import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import com.tondracek.myfarmer.user.domain.model.SystemUser
import com.tondracek.myfarmer.user.domain.model.UserId
import com.tondracek.myfarmer.user.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetUsersByIdsUC @Inject constructor(
    private val repository: UserRepository,
) {

    operator fun invoke(ids: List<UserId>): Flow<DomainResult<List<SystemUser>>> {
        if (ids.isEmpty())
            return flowOf(DomainResult.Success(emptyList()))

        return repository.getByIds(ids)
    }
}