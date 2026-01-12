package com.tondracek.myfarmer.ui.auth.common.google

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.tondracek.myfarmer.R

@Composable
fun rememberGoogleSignInClient(): GoogleSignInClient {

    val context = LocalContext.current

    val serverClientId = stringResource(R.string.default_web_client_id)
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(serverClientId)
            .requestEmail()
            .build()
    }

    return remember { GoogleSignIn.getClient(context, gso) }
}
