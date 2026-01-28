package com.tondracek.myfarmer.user.domain.usecase

import com.tondracek.myfarmer.auth.domain.model.AuthId
import com.tondracek.myfarmer.auth.domain.repository.AuthRepository
import com.tondracek.myfarmer.core.domain.domainerror.AuthError
import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import com.tondracek.myfarmer.core.domain.usecaseresult.flatMap
import com.tondracek.myfarmer.user.domain.model.SystemUser
import com.tondracek.myfarmer.user.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

/**
 * Gets the currently logged-in user
 * @return [DomainResult.Success] with [SystemUser] if logged in,
 * [DomainResult.Failure] if no user is logged in.
 */
class GetLoggedInUserUC @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {

    operator fun invoke(): Flow<DomainResult<SystemUser>> =
        authRepository.getCurrentUserAuthId().flatMap { firebaseId ->
            when (firebaseId == null) {
                true -> flowOf(DomainResult.Failure(AuthError.NotLoggedIn))
                false -> getOrCreateSystemUser(firebaseId)
            }
        }

    private fun getOrCreateSystemUser(authId: AuthId): Flow<DomainResult<SystemUser>> =
        userRepository.getUserByAuthId(authId).flatMap { user ->
            when (user == null) {
                true -> SystemUser.createEmpty(authId)
                    .let { userRepository.create(it) }
                    .flatMap { userRepository.getById(it) }
                    .filter { it is DomainResult.Success }

                false -> flowOf(DomainResult.Success(user))
            }
        }
}