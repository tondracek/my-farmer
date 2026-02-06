package com.tondracek.myfarmer.user.domain.usecase

import com.tondracek.myfarmer.auth.domain.model.AuthId
import com.tondracek.myfarmer.auth.domain.repository.AuthRepository
import com.tondracek.myfarmer.core.domain.domainerror.AuthError
import com.tondracek.myfarmer.core.domain.domainresult.DomainResult
import com.tondracek.myfarmer.core.domain.domainresult.flatMap
import com.tondracek.myfarmer.core.domain.domainresult.mapFlatten
import com.tondracek.myfarmer.core.domain.domainresult.mapFlowUC
import com.tondracek.myfarmer.user.domain.model.SystemUser
import com.tondracek.myfarmer.user.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
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
        authRepository.getCurrentUserAuthIdFlow().flatMap { authId ->
            when (authId == null) {
                true -> flowOf(DomainResult.Failure(AuthError.NotLoggedIn))
                false -> getOrCreateSystemUser(authId)
            }
        }

    suspend fun sync(): DomainResult<SystemUser> =
        authRepository.getCurrentUserAuthId().mapFlatten { authId ->
            when (authId == null) {
                true -> DomainResult.Failure(AuthError.NotLoggedIn)
                false -> getOrCreateSystemUser(authId).first()
            }
        }

    private fun getOrCreateSystemUser(authId: AuthId): Flow<DomainResult<SystemUser>> =
        userRepository.getUserByAuthId(authId).mapFlowUC { user ->
            when (user == null) {
                true -> createAndReturnUser(authId)
                false -> DomainResult.Success(user)
            }
        }

    private suspend fun createAndReturnUser(authId: AuthId): DomainResult<SystemUser> =
        flowOf(SystemUser.createEmpty(authId))
            .map { userRepository.create(it) }
            .flatMap { userRepository.getById(it) }
            .first { it is DomainResult.Success }
}