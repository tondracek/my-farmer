package com.tondracek.myfarmer.systemuser.data

import com.tondracek.myfarmer.auth.domain.model.AuthId
import com.tondracek.myfarmer.core.repository.BaseRepository
import com.tondracek.myfarmer.core.repository.IdMapper
import com.tondracek.myfarmer.core.repository.RepositoryCore
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntityId
import com.tondracek.myfarmer.core.repository.request.filterEq
import com.tondracek.myfarmer.core.repository.request.filterIn
import com.tondracek.myfarmer.core.repository.request.repositoryRequest
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import com.tondracek.myfarmer.systemuser.domain.model.UserId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    mapper: UserEntityMapper,
    core: RepositoryCore<UserEntity, FirestoreEntityId>,
) : BaseRepository<SystemUser, UserId, UserEntity, String>(
    core = core,
    entityMapper = mapper,
    idMapper = object : IdMapper<UserId, String> {
        override fun toEntityId(modelId: UserId): String = modelId.toString()
        override fun toModelId(entityId: String): UserId = UserId.fromString(entityId)
    }
) {

    fun getUserByAuthId(authId: AuthId): Flow<SystemUser?> =
        get(repositoryRequest {
            addFilter(UserEntity::firebaseId filterEq authId.value)
        }).map {
            it.firstOrNull()
        }

    fun getByIds(userIds: List<UserId>) = get(
        repositoryRequest {
            addFilter(UserEntity::id filterIn userIds.map { it.toString() })
        }
    )
}