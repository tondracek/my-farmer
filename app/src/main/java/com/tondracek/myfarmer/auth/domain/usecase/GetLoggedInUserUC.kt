package com.tondracek.myfarmer.auth.domain.usecase

import com.tondracek.myfarmer.auth.data.FirebaseAuthRepository
import com.tondracek.myfarmer.auth.domain.usecase.result.NotLoggedInUCResult
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.systemuser.data.UserRepository
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Gets the currently logged-in user
 * @return [UCResult.Success] with [SystemUser] if logged in,
 *         [NotLoggedInUCResult] if no user is logged in
 */
class GetLoggedInUserUC @Inject constructor(
    private val authRepository: FirebaseAuthRepository,
    private val userRepository: UserRepository,
) : () -> Flow<UCResult<SystemUser>> {


    @OptIn(ExperimentalCoroutinesApi::class)
    override operator fun invoke(): Flow<UCResult<SystemUser>> =
        authRepository.getCurrentUserFirebaseId().flatMapLatest { firebaseId ->
            when (firebaseId == null) {
                true -> flowOf(NotLoggedInUCResult())
                false -> getOrCreateSystemUser(firebaseId)
                    .map { UCResult.Success(it) }
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getOrCreateSystemUser(firebaseId: String): Flow<SystemUser> =
        userRepository.getUserByFirebaseId(firebaseId).flatMapLatest { user ->
            when (user == null) {
                true -> SystemUser.createEmpty(firebaseId)
                    .let { userRepository.create(it) }
                    .let { userRepository.getById(it) }
                    .filterNotNull()

                false -> flowOf(user)
            }
        }
}