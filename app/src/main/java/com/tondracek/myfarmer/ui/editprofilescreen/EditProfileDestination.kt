package com.tondracek.myfarmer.ui.editprofilescreen

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.common.scaffold.ScreenScaffold
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.core.navigation.routeDestination

fun NavGraphBuilder.editProfileDestination(
    navController: NavController,
) = routeDestination<Route.EditProfileScreenRoute> { appUiController ->
    val viewmodel: EditProfileViewModel = hiltViewModel()
    val state by viewmodel.state.collectAsState()

    val profileSavedMessage = stringResource(R.string.profile_saved_successfully)
    LaunchedEffect(Unit) {
        viewmodel.effects.collect {
            when (it) {
                EditProfileScreenEffect.GoBack -> navController.navigateUp()
                EditProfileScreenEffect.OpenAuthScreen -> navController.navigate(Route.AuthScreenRoute)
                EditProfileScreenEffect.ShowSavedProfileMessage ->
                    appUiController.showSuccessMessage(profileSavedMessage)

                is EditProfileScreenEffect.ShowError ->
                    appUiController.showErrorMessage(it.message)
            }
        }
    }

    ScreenScaffold(
        title = stringResource(R.string.edit_profile)
    ) {
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
