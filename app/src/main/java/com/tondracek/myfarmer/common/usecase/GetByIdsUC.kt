package com.tondracek.myfarmer.common.usecase

import com.tondracek.myfarmer.core.repository.Repository
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntity
import com.tondracek.myfarmer.core.repository.request.filterIn
import com.tondracek.myfarmer.core.repository.request.repositoryRequest
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class GetByIdsUC<Model> @Inject constructor(
    private val repository: Repository<Model>,
) {

    operator fun invoke(ids: List<UUID>): Flow<UCResult<List<Model>>> {
        if (ids.isEmpty())
            return flowOf(UCResult.Success(emptyList()))

        return repository.get(repositoryRequest {
            FirestoreEntity::id filterIn ids.map(UUID::toString)
        }).map { result -> UCResult.Success(result) }
    }
}
