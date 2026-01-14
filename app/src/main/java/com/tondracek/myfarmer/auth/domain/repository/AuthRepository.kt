package com.tondracek.myfarmer.auth.domain.repository

import com.tondracek.myfarmer.auth.domain.model.AuthId
import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun loginUser(
        email: String,
        password: String
    ): DomainResult<Unit>

    suspend fun loginWithGoogle(
        idToken: String
    ): DomainResult<Unit>

    suspend fun registerUser(
        email: String,
        password: String
    ): DomainResult<Unit>

    fun signOut(): DomainResult<Unit>

    fun getCurrentUserAuthId(): Flow<DomainResult<AuthId?>>

    fun isLoggedIn(): Flow<DomainResult<Boolean>>
}
