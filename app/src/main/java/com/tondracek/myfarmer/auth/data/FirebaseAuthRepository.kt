package com.tondracek.myfarmer.auth.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.tondracek.myfarmer.auth.domain.model.AuthId
import com.tondracek.myfarmer.auth.domain.repository.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : AuthRepository {

    override suspend fun loginUser(email: String, password: String) {
        firebaseAuth
            .signInWithEmailAndPassword(email, password)
            .await()
    }

    override suspend fun loginWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth
            .signInWithCredential(credential)
            .await()
    }

    override suspend fun registerUser(email: String, password: String) {
        firebaseAuth
            .createUserWithEmailAndPassword(email, password)
            .await()
    }

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun getCurrentUserAuthId(): Flow<AuthId?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            trySend(user?.uid?.let { AuthId(it) })
        }

        auth.addAuthStateListener(listener)

        awaitClose {
            auth.removeAuthStateListener(listener)
        }
    }

    override fun isLoggedIn(): Flow<Boolean> =
        getCurrentUserAuthId().map { it != null }

    override fun signOut() = auth.signOut()
}
