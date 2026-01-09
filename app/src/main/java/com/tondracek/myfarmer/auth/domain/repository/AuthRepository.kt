package com.tondracek.myfarmer.auth.domain.repository

import com.tondracek.myfarmer.auth.domain.model.AuthId
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getCurrentUserAuthId(): Flow<AuthId?>
    fun isLoggedIn(): Flow<Boolean>
    fun signOut()
}