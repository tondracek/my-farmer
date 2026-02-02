package com.tondracek.myfarmer.auth.domain.usecase

import com.tondracek.myfarmer.auth.domain.repository.AuthRepository
import com.tondracek.myfarmer.core.domain.domainresult.DomainResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsLoggedInUC @Inject constructor(
    private val authRepository: AuthRepository,
) : () -> Flow<DomainResult<Boolean>> {

    override operator fun invoke(): Flow<DomainResult<Boolean>> =
        authRepository.isLoggedIn()
}