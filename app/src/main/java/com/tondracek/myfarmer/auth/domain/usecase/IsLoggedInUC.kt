package com.tondracek.myfarmer.auth.domain.usecase

import com.tondracek.myfarmer.auth.data.FirebaseAuthRepository
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class IsLoggedInUC @Inject constructor(
    private val authRepository: FirebaseAuthRepository,
) : () -> Flow<UCResult<Boolean>> {

    override operator fun invoke(): Flow<UCResult<Boolean>> =
        authRepository.isLoggedIn()
            .map { UCResult.Success(it) }
}