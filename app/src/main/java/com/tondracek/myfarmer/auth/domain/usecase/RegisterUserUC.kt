package com.tondracek.myfarmer.auth.domain.usecase

import com.tondracek.myfarmer.auth.domain.repository.AuthRepository
import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult
import javax.inject.Inject

class RegisterUserUC @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
    ): UCResult<Unit> = UCResult.of("Failed to register user.") {
        authRepository.registerUser(
            email = email,
            password = password,
        )
    }
}