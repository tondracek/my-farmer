package com.tondracek.myfarmer.ui.core.navigation

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tondracek.myfarmer.ui.core.appstate.APP_UI_STATE_APPLY_TOP_BAR_PADDING_DEFAULT
import com.tondracek.myfarmer.ui.core.appstate.AppUiState
import com.tondracek.myfarmer.ui.core.appstate.LocalAppUiController

inline fun <reified T : Route> NavGraphBuilder.routeDestination(
    @StringRes titleId: Int? = null,
    applyTopBarPadding: Boolean = APP_UI_STATE_APPLY_TOP_BAR_PADDING_DEFAULT,
    crossinline content: @Composable () -> Unit
) = composable<T> {
    val appUiState = getAppUiState(
        titleId = titleId,
        applyTopBarPadding = applyTopBarPadding,
    )

    val appUiController = LocalAppUiController.current
    LaunchedEffect(Unit) {
        appUiController.apply(appUiState)
    }

    content()
}

@Composable
fun getAppUiState(
    titleId: Int? = null,
    applyTopBarPadding: Boolean? = null,
) = AppUiState().let { base ->
    val title = titleId?.let { stringResource(id = it) }
        ?: base.title
    val applyTopBarPadding = applyTopBarPadding
        ?: base.applyTopBarPadding

    AppUiState(
        title = title,
        applyTopBarPadding = applyTopBarPadding
    )
}
