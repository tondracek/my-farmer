package com.tondracek.myfarmer.auth.domain.usecase

import com.tondracek.myfarmer.auth.domain.model.AuthId
import com.tondracek.myfarmer.auth.domain.repository.AuthRepository
import com.tondracek.myfarmer.core.domain.domainerror.AuthError
import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult
import com.tondracek.myfarmer.core.domain.usecaseresult.flatMap
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import com.tondracek.myfarmer.systemuser.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

/**
 * Gets the currently logged-in user
 * @return [UCResult.Success] with [SystemUser] if logged in,
 *         [NotLoggedInUCResult] if no user is logged in
 */
class GetLoggedInUserUC @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {

    operator fun invoke(): Flow<UCResult<SystemUser>> =
        authRepository.getCurrentUserAuthId().flatMap { firebaseId ->
            when (firebaseId == null) {
                true -> flowOf(UCResult.Failure(AuthError.NotLoggedIn))
                false -> getOrCreateSystemUser(firebaseId)
            }
        }

    private fun getOrCreateSystemUser(authId: AuthId): Flow<UCResult<SystemUser>> =
        userRepository.getUserByAuthId(authId).flatMap { user ->
            when (user == null) {
                true -> SystemUser.createEmpty(authId)
                    .let { userRepository.create(it) }
                    .flatMap { userRepository.getById(it) }
                    .filter { it is UCResult.Success }

                false -> flowOf(UCResult.Success(user))
            }
        }
}