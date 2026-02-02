package com.tondracek.myfarmer.auth.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.GoogleAuthProvider
import com.tondracek.myfarmer.auth.domain.model.AuthId
import com.tondracek.myfarmer.auth.domain.repository.AuthRepository
import com.tondracek.myfarmer.core.data.domainResultOf
import com.tondracek.myfarmer.core.domain.domainerror.AuthError
import com.tondracek.myfarmer.core.domain.domainresult.DomainResult
import com.tondracek.myfarmer.core.domain.domainresult.mapFlow
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

    override suspend fun loginUser(email: String, password: String): DomainResult<Unit> =
        domainResultOf(
            AuthError.Unknown,
            FirebaseAuthInvalidUserException::class to AuthError.UserNotFound,
            FirebaseAuthInvalidCredentialsException::class to AuthError.InvalidCredentials,
            IllegalArgumentException::class to AuthError.InvalidCredentials,
        ) {
            firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .await()
        }

    override suspend fun loginWithGoogle(idToken: String): DomainResult<Unit> = domainResultOf(
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

    override suspend fun registerUser(email: String, password: String): DomainResult<Unit> =
        domainResultOf(
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

    override fun getCurrentUserAuthId(): Flow<DomainResult<AuthId?>> =
        callbackFlow<DomainResult<AuthId?>> {
            val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
                val user = firebaseAuth.currentUser
                val authId = user?.uid?.let { AuthId(it) }
                trySend(DomainResult.Success(authId))
            }

            auth.addAuthStateListener(listener)

            awaitClose {
                auth.removeAuthStateListener(listener)
            }
        }.catch {
            emit(DomainResult.Failure(AuthError.Unknown, it))
        }

    override fun isLoggedIn(): Flow<DomainResult<Boolean>> =
        getCurrentUserAuthId().mapFlow { it != null }

    override fun signOut() =
        domainResultOf(AuthError.LogoutFailed) { auth.signOut() }

    override suspend fun sendPasswordResetEmail(email: String): DomainResult<Unit> =
        domainResultOf(AuthError.Unknown) {
            auth.sendPasswordResetEmail(email).await()
        }
}
