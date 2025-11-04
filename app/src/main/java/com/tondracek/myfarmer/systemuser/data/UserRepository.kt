package com.tondracek.myfarmer.systemuser.data

import com.tondracek.myfarmer.core.di.RepositoryCoreFactory
import com.tondracek.myfarmer.core.repository.BaseRepository
import com.tondracek.myfarmer.core.repository.request.filterEq
import com.tondracek.myfarmer.core.repository.request.repositoryRequest
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    factory: RepositoryCoreFactory,
    mapper: UserEntityMapper,
) : BaseRepository<SystemUser>(factory.create(mapper, UserEntity::class.java)) {

    fun getUserByFirebaseId(firebaseId: String): Flow<SystemUser?> =
        get(repositoryRequest {
            addFilter(UserEntity::firebaseId filterEq firebaseId)
        }).map {
            it.firstOrNull()
        }
}