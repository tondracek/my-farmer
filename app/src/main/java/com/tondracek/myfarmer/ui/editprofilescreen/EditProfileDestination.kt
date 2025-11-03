package com.tondracek.myfarmer.ui.editprofilescreen

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tondracek.myfarmer.ui.core.navigation.AppNavigator
import kotlinx.serialization.Serializable

@Serializable
data object EditProfileRoute

fun AppNavigator.navigateToEditProfileScreen() =
    navigate(EditProfileRoute)

fun NavGraphBuilder.editProfileDestination() {
    composable<EditProfileRoute> {
        val viewmodel: EditProfileViewModel = hiltViewModel()
        val state by viewmodel.state.collectAsState()

        EditProfileScreen(
            state = state,
            onNameChange = viewmodel::onNameChange,
            onProfilePictureChange = viewmodel::onProfilePictureChange,
            onContactInfoChange = viewmodel::onContactInfoChange,
        )
    }
}


