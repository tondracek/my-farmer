package com.tondracek.myfarmer.ui.authscreen

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.tondracek.myfarmer.ui.core.navigation.AppNavigator
import com.tondracek.myfarmer.ui.core.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthScreenViewModel @Inject constructor(
    private val appNavigator: AppNavigator,
) : ViewModel() {

    fun navigateToProfileScreen() =
        appNavigator.navigate(Route.EditProfileScreenRoute)

    fun showError(context: Context, message: String) =
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_LONG
        ).show()
}