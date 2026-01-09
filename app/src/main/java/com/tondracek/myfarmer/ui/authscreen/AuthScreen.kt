package com.tondracek.myfarmer.ui.authscreen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.tondracek.myfarmer.R

@Composable
fun FirebaseUiLoginScreen(onSuccess: () -> Unit, onError: (String) -> Unit) {
    val providers = listOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build(),
    )

    val signInIntent = remember {
        AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.mipmap.app_icon)
            .setTheme(R.style.Theme_MyFarmer_FirebaseAuth)
            .build()
    }

    val loginCancelledText = stringResource(R.string.login_cancelled)
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val response = IdpResponse.fromResultIntent(result.data)

        val user = FirebaseAuth.getInstance().currentUser
        when (user) {
            null -> onError(response?.error?.message ?: loginCancelledText)
            else -> onSuccess()
        }
    }

    LaunchedEffect(Unit) {
        launcher.launch(signInIntent)
    }
}
