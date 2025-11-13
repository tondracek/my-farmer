package com.tondracek.myfarmer.ui.editprofilescreen

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tondracek.myfarmer.ui.core.navigation.AppNavigator
import com.tondracek.myfarmer.ui.core.navigation.Route

fun AppNavigator.navigateToEditProfileScreen() =
    navigate(Route.EditProfileScreenRoute)

fun NavGraphBuilder.editProfileDestination() {
    composable<Route.EditProfileScreenRoute> {
        val viewmodel: EditProfileViewModel = hiltViewModel()
        val state by viewmodel.state.collectAsState()

        EditProfileScreen(
            state = state,
            onNameChange = viewmodel::onNameChange,
            onProfilePictureChange = viewmodel::onProfilePictureChange,
            onContactInfoChange = viewmodel::onContactInfoChange,
            onLogout = viewmodel::onLogout,
            onSaveClick = viewmodel::onSaveProfile,
        )
    }
}


