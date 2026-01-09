package com.tondracek.myfarmer.auth.data

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthRepository @Inject constructor() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getCurrentUserFirebaseId(): Flow<String?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            trySend(user?.uid)
        }

        auth.addAuthStateListener(listener)

        awaitClose {
            auth.removeAuthStateListener(listener)
        }
    }


    fun isLoggedIn(): Flow<Boolean> =
        getCurrentUserFirebaseId().map { it != null }
}
