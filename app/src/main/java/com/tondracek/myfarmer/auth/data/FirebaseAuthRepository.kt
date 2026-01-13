package com.tondracek.myfarmer.auth.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.GoogleAuthProvider
import com.tondracek.myfarmer.auth.domain.model.AuthId
import com.tondracek.myfarmer.auth.domain.repository.AuthRepository
import com.tondracek.myfarmer.core.domain.domainerror.AuthError
import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult
import com.tondracek.myfarmer.core.domain.usecaseresult.mapFlow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : AuthRepository {

    override suspend fun loginUser(email: String, password: String): UCResult<Unit> = UCResult.of(
        AuthError.Unknown,
        FirebaseAuthInvalidUserException::class to AuthError.UserNotFound,
        FirebaseAuthInvalidCredentialsException::class to AuthError.InvalidCredentials,
    ) {
        firebaseAuth
            .signInWithEmailAndPassword(email, password)
            .await()
    }

    override suspend fun loginWithGoogle(idToken: String): UCResult<Unit> = UCResult.of(
        AuthError.Unknown,
        FirebaseAuthInvalidUserException::class to AuthError.UserNotFound,
        FirebaseAuthInvalidCredentialsException::class to AuthError.InvalidCredentials,
        FirebaseAuthUserCollisionException::class to AuthError.EmailAlreadyInUse,
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth
            .signInWithCredential(credential)
            .await()
    }

    override suspend fun registerUser(email: String, password: String): UCResult<Unit> =
        UCResult.of(
            AuthError.Unknown,
            FirebaseAuthUserCollisionException::class to AuthError.EmailAlreadyInUse,
            FirebaseAuthInvalidCredentialsException::class to AuthError.InvalidCredentials,
            FirebaseAuthWeakPasswordException::class to AuthError.WeakPassword,
        ) {
            firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .await()
        }

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun getCurrentUserAuthId(): Flow<UCResult<AuthId?>> = callbackFlow<UCResult<AuthId?>> {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            val authId = user?.uid?.let { AuthId(it) }
            trySend(UCResult.Success(authId))
        }

        auth.addAuthStateListener(listener)

        awaitClose {
            auth.removeAuthStateListener(listener)
        }
    }.catch {
        emit(UCResult.Failure(AuthError.Unknown, it))
    }

    override fun isLoggedIn(): Flow<UCResult<Boolean>> =
        getCurrentUserAuthId().mapFlow { it != null }

    override fun signOut() =
        UCResult.of(AuthError.LogoutFailed) { auth.signOut() }
}
