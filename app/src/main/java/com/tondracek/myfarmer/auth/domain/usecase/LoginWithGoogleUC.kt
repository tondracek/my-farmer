package com.tondracek.myfarmer.auth.domain.usecase

import com.tondracek.myfarmer.auth.domain.repository.AuthRepository
import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult
import javax.inject.Inject

class LoginWithGoogleUC @Inject constructor(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(
        idToken: String,
    ): UCResult<Unit> =
        authRepository.loginWithGoogle(idToken)
}
