package com.tondracek.myfarmer.ui.common.scaffold

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.tondracek.myfarmer.ui.common.navbar.BottomNavigationBar
import com.tondracek.myfarmer.ui.common.navbar.NavBarViewModel
import com.tondracek.myfarmer.ui.core.appstate.AppUiController
import com.tondracek.myfarmer.ui.core.appstate.LocalAppUiController
import com.tondracek.myfarmer.ui.core.confirmationdialog.ConfirmationDialog
import com.tondracek.myfarmer.ui.core.confirmationdialog.ConfirmationDialogRequest
import com.tondracek.myfarmer.ui.core.navigation.NavGraph
import com.tondracek.myfarmer.ui.core.navigation.isInNavGraph
import com.tondracek.myfarmer.ui.core.snackbar.MyFarmerSnackBar
import com.tondracek.myfarmer.ui.core.snackbar.SnackBarType
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.core.util.toUserFriendlyMessage
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

@Composable
fun AppScaffold(
    navController: NavController,
    appUiController: AppUiController,
    content: @Composable BoxScope.() -> Unit,
) {
    val context = LocalContext.current
    val density = LocalDensity.current

    val navBarViewModel: NavBarViewModel = hiltViewModel()
    val navBarState by navBarViewModel.state.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        val messageToTypeFlow = merge(
            appUiController.errorMessages.map { it to SnackBarType.ERROR },
            appUiController.errors.map { it.toUserFriendlyMessage(context) to SnackBarType.ERROR },
            appUiController.successMessages.map { it to SnackBarType.SUCCESS },
        )
        messageToTypeFlow.collect { (message, type) ->
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = type,
                duration = SnackbarDuration.Short,
            )
        }
    }

    val showBottomBar = navController.isInNavGraph(NavGraph.MainFlow)

    CompositionLocalProvider(
        LocalAppUiController provides appUiController,
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MyFarmerTheme.colors.surface,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .appBackground()
                    .windowInsetsPadding(WindowInsets.systemBars)
            ) {
                var navBarHeight by remember { mutableStateOf(0.dp) }
                val bottomContentPadding = if (showBottomBar) navBarHeight else 0.dp

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = bottomContentPadding)
                        .imeWithoutAppBottomBar(bottomContentPadding),
                ) {
                    content()

                    SnackbarHost(
                        hostState = snackbarHostState,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp),
                        snackbar = { MyFarmerSnackBar(snackbarData = it) }
                    )
                }

                if (showBottomBar) {
                    BottomNavigationBar(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .onGloballyPositioned { coordinates ->
                                with(density) { navBarHeight = coordinates.size.height.toDp() }
                            },
                        state = navBarState,
                        navController = navController,
                    )
                }
            }
        }
    }

    var confirmationDialog by remember { mutableStateOf<ConfirmationDialogRequest?>(null) }
    LaunchedEffect(Unit) {
        appUiController.confirmationDialogs.collect { dialogRequest ->
            confirmationDialog = dialogRequest
        }
    }

    ConfirmationDialog(
        confirmationDialog = confirmationDialog,
        onDismissRequest = { confirmationDialog = null },
    )

    val view = LocalView.current
    val navigationBarColor = MyFarmerTheme.colors.surfaceContainer
    LaunchedEffect(navigationBarColor) {
        val window = (view.context as Activity).window
        window.navigationBarColor = navigationBarColor.toArgb()
    }
}

@Composable
private fun Modifier.imeWithoutAppBottomBar(bottomBarHeight: Dp): Modifier =
    windowInsetsPadding(WindowInsets.ime.exclude(WindowInsets(bottom = bottomBarHeight)))

@Composable
private fun Modifier.appBackground(): Modifier {
    val colorA = MyFarmerTheme.colors.primaryContainer
    val colorB = MyFarmerTheme.colors.surface

    val customBrush = remember {
        object : ShaderBrush() {
            override fun createShader(size: Size) = RadialGradientShader(
                colors = listOf(colorA, colorB),
                center = Offset(0f, 0f),
                radius = size.maxDimension * 0.4f,
            )
        }
    }

    return this.background(customBrush)
}