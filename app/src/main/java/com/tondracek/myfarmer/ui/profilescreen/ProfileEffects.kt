package com.tondracek.myfarmer.ui.profilescreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.core.appstate.AppUiController
import com.tondracek.myfarmer.ui.core.navigation.NavGraph
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.core.navigation.navigateToGraph
import com.tondracek.myfarmer.ui.core.viewmodel.CollectEffects

@Composable
fun ProfileEffects(
    navController: NavController,
    appUiController: AppUiController,
    viewModel: ProfileScreenViewModel,
) {
    val logoutConfirmationMessage = stringResource(R.string.are_you_sure_you_want_to_log_out)
    val logoutSuccessfulMessage = stringResource(R.string.logout_successful)

    viewModel.CollectEffects { effect ->
        when (effect) {
            ProfileScreenEffect.GoToAuth ->
                navController.navigateToGraph(NavGraph.MainFlow.Auth, saveState = false)

            ProfileScreenEffect.GoToEditProfile ->
                navController.navigate(Route.EditProfileScreenRoute)

            is ProfileScreenEffect.ShowError ->
                appUiController.showError(effect.error)

            ProfileScreenEffect.RequestLogoutConfirmation ->
                appUiController.raiseConfirmationDialog(
                    message = logoutConfirmationMessage,
                    onConfirm = { viewModel.onEvent(ProfileScreenEvent.OnLogoutConfirmed) }
                )

            is ProfileScreenEffect.ShowErrorMessage ->
                appUiController.showErrorMessage(effect.errorMessage)

            ProfileScreenEffect.ShowLogoutSuccessMessage ->
                appUiController.showSuccessMessage(logoutSuccessfulMessage)
        }
    }
}
