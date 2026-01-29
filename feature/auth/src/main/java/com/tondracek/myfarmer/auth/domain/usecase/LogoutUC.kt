package com.tondracek.myfarmer.auth.domain.usecase

import com.tondracek.myfarmer.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUC @Inject constructor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke() = authRepository.signOut()
}
