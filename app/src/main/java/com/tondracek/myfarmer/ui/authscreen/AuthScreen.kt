package com.tondracek.myfarmer.ui.authscreen

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
//            .setLogo(R.drawable.ic_my_logo)
            .setTheme(R.style.Theme_MyFarmer)
            .build()
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val response = IdpResponse.fromResultIntent(result.data)
        if (result.resultCode == Activity.RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) onSuccess()
        } else {
            onError(response?.error?.message ?: "Login cancelled")
        }
    }

    LaunchedEffect(Unit) {
        launcher.launch(signInIntent)
    }
}
