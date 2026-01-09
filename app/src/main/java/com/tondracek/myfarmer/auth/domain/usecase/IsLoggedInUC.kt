package com.tondracek.myfarmer.auth.domain.usecase

import com.tondracek.myfarmer.auth.domain.repository.AuthRepository
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.core.usecaseresult.toUCResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsLoggedInUC @Inject constructor(
    private val authRepository: AuthRepository,
) : () -> Flow<UCResult<Boolean>> {

    override operator fun invoke(): Flow<UCResult<Boolean>> =
        authRepository.isLoggedIn().toUCResult()
}