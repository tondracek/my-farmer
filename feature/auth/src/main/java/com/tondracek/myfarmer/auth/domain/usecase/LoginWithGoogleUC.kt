package com.tondracek.myfarmer.auth.domain.usecase

import com.tondracek.myfarmer.auth.domain.repository.AuthRepository
import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import javax.inject.Inject

class LoginWithGoogleUC @Inject constructor(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(
        idToken: String,
    ): DomainResult<Unit> =
        authRepository.loginWithGoogle(idToken)
}
