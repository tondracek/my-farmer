package com.tondracek.myfarmer.auth.domain.usecase

import com.tondracek.myfarmer.auth.domain.model.AuthId
import com.tondracek.myfarmer.auth.domain.repository.AuthRepository
import com.tondracek.myfarmer.auth.domain.usecase.result.NotLoggedInUCResult
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.core.usecaseresult.toUCResult
import com.tondracek.myfarmer.systemuser.data.UserRepository
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
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
) : () -> Flow<UCResult<SystemUser>> {


    @OptIn(ExperimentalCoroutinesApi::class)
    override operator fun invoke(): Flow<UCResult<SystemUser>> =
        authRepository.getCurrentUserAuthId().flatMapLatest { firebaseId ->
            when (firebaseId == null) {
                true -> flowOf(NotLoggedInUCResult())
                false -> getOrCreateSystemUser(firebaseId)
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getOrCreateSystemUser(authId: AuthId): Flow<UCResult<SystemUser>> =
        userRepository.getUserByAuthId(authId).flatMapLatest { user ->
            when (user == null) {
                true -> SystemUser.createEmpty(authId)
                    .let { userRepository.create(it) }
                    .let { userRepository.getById(it) }
                    .filterNotNull()
                    .toUCResult("Could not create a new SystemUser.")

                false -> flowOf(user).toUCResult()
            }
        }
}