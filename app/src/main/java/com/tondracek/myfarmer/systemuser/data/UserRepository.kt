package com.tondracek.myfarmer.systemuser.data

import com.tondracek.myfarmer.auth.data.FirebaseAuthRepository
import com.tondracek.myfarmer.core.di.RepositoryCoreFactory
import com.tondracek.myfarmer.core.repository.BaseRepository
import com.tondracek.myfarmer.core.repository.request.filterEq
import com.tondracek.myfarmer.core.repository.request.filterIn
import com.tondracek.myfarmer.core.repository.request.repositoryRequest
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import com.tondracek.myfarmer.systemuser.domain.model.UserId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val authRepository: FirebaseAuthRepository,
    factory: RepositoryCoreFactory,
    mapper: UserEntityMapper,
) : BaseRepository<SystemUser>(factory.create(mapper, UserEntity::class.java)) {

    fun getUserByFirebaseId(firebaseId: String): Flow<SystemUser?> =
        get(repositoryRequest {
            addFilter(UserEntity::firebaseId filterEq firebaseId)
        }).map {
            it.firstOrNull()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getLoggedInUser(): Flow<SystemUser?> = authRepository
        .getCurrentUserFirebaseId()
        .flatMapLatest {
            when (it) {
                null -> flowOf(null)
                else -> getUserByFirebaseId(it)
            }
        }

    fun getByIds(userIds: List<UserId>) = get(
        repositoryRequest {
            addFilter(UserEntity::id filterIn userIds.map { it.toString() })
        }
    )
}