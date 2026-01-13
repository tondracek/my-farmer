package com.tondracek.myfarmer.auth.domain.repository

import com.tondracek.myfarmer.auth.domain.model.AuthId
import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun loginUser(
        email: String,
        password: String
    ): UCResult<Unit>

    suspend fun loginWithGoogle(
        idToken: String
    ): UCResult<Unit>

    suspend fun registerUser(
        email: String,
        password: String
    ): UCResult<Unit>

    fun signOut(): UCResult<Unit>

    fun getCurrentUserAuthId(): Flow<UCResult<AuthId?>>

    fun isLoggedIn(): Flow<UCResult<Boolean>>
}
