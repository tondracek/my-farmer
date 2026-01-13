package com.tondracek.myfarmer.auth.domain.usecase

import com.tondracek.myfarmer.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUserUC @Inject constructor(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(email: String, password: String) =
        authRepository.loginUser(email, password)
}