package com.tondracek.myfarmer.ui.editprofilescreen

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.common.scaffold.ScreenScaffold
import com.tondracek.myfarmer.ui.core.appstate.AppUiController
import com.tondracek.myfarmer.ui.core.navigation.NavGraph
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.core.navigation.navigateToGraph
import com.tondracek.myfarmer.ui.core.viewmodel.CollectEffects

fun NavGraphBuilder.editProfileDestination(
    navController: NavController,
    appUiController: AppUiController,
) = composable<Route.EditProfileScreenRoute> {
    val viewmodel: EditProfileViewModel = hiltViewModel()
    val state by viewmodel.state.collectAsState()

    EditProfileEffects(
        viewmodel = viewmodel,
        navController = navController,
        appUiController = appUiController
    )

    ScreenScaffold(
        title = stringResource(R.string.edit_profile)
    ) {
        EditProfileScreen(
            state = state,
            onFormEvent = viewmodel::onFormEvent,
            onScreenEvent = viewmodel::onScreenEvent,
        )
    }

    BackHandler {
        when (state) {
            is EditProfileScreenState.Success ->
                viewmodel.onScreenEvent(EditProfileScreenEvent.OnCancelClicked)

            else -> navController.navigateUp()
        }
    }
}

@Composable
private fun EditProfileEffects(
    viewmodel: EditProfileViewModel,
    navController: NavController,
    appUiController: AppUiController
) {
    val discardChangesMessage = stringResource(R.string.discard_changes_message)
    val profileSavedMessage = stringResource(R.string.profile_saved_successfully)

    viewmodel.CollectEffects { effect ->
        when (effect) {
            EditProfileScreenEffect.GoToAuth ->
                navController.navigateToGraph(NavGraph.MainFlow.Auth, saveState = false)

            EditProfileScreenEffect.NavigateBack ->
                navController.navigateUp()

            EditProfileScreenEffect.ShowSavedProfileMessage ->
                appUiController.showSuccessMessage(profileSavedMessage)

            is EditProfileScreenEffect.ShowError ->
                appUiController.showError(effect.error)

            EditProfileScreenEffect.RequestCancelConfirmation -> appUiController.raiseConfirmationDialog(
                message = discardChangesMessage,
                onConfirm = { viewmodel.onScreenEvent(EditProfileScreenEvent.OnCancelConfirmed) },
            )
        }
    }
}
