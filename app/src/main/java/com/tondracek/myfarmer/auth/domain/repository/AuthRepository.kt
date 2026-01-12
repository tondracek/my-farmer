package com.tondracek.myfarmer.auth.domain.repository

import com.tondracek.myfarmer.auth.domain.model.AuthId
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun loginUser(email: String, password: String)
    suspend fun loginWithGoogle(idToken: String)

    suspend fun registerUser(email: String, password: String)

    fun signOut()

    fun getCurrentUserAuthId(): Flow<AuthId?>
    fun isLoggedIn(): Flow<Boolean>
}