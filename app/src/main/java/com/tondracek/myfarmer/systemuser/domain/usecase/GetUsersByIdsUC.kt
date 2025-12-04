package com.tondracek.myfarmer.systemuser.domain.usecase

import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntity
import com.tondracek.myfarmer.core.repository.request.filterIn
import com.tondracek.myfarmer.core.repository.request.repositoryRequest
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.systemuser.data.UserRepository
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class GetUsersByIdsUC @Inject constructor(
    private val repository: UserRepository,
) {

    operator fun invoke(ids: List<UUID>): Flow<UCResult<List<SystemUser>>> {
        if (ids.isEmpty())
            return flowOf(UCResult.Success(emptyList()))

        return repository.get(repositoryRequest {
            FirestoreEntity::id filterIn ids.map(UUID::toString)
        }).map { result -> UCResult.Success(result) }
    }
}
