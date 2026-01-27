package com.tondracek.myfarmer.ui.auth.common.google

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.GetCredentialInterruptedException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.tondracek.myfarmer.core.domain.domainerror.AuthError
import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import timber.log.Timber

suspend fun signInWithGoogleCredentialManager(
    context: Context,
    serverClientId: String,
): DomainResult<String?> {
    val credentialManager = CredentialManager.create(context)

    val googleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(serverClientId)
        .build()

    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    return try {
        val result = credentialManager.getCredential(
            context = context,
            request = request,
        )

        val credential = result.credential
        if (credential is CustomCredential
            && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            val googleCredential =
                GoogleIdTokenCredential.createFrom(credential.data)

            DomainResult.Success(googleCredential.idToken)
        } else {
            DomainResult.Failure(AuthError.GoogleSignInFailedError)
        }
    } catch (e: GetCredentialCancellationException) {
        Timber.w(e, "Google Sign-In was cancelled by the user.")
        DomainResult.Success(null)
    } catch (e: GetCredentialInterruptedException) {
        DomainResult.Failure(AuthError.GoogleSignInCancelledError, e)
    } catch (e: GetCredentialException) {
        DomainResult.Failure(AuthError.GoogleSignInFailedError, e)
    }
}
