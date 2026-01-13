package com.tondracek.myfarmer.ui.common.scaffold

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.tondracek.myfarmer.ui.common.navbar.BottomNavigationBar
import com.tondracek.myfarmer.ui.common.navbar.NavBarViewModel
import com.tondracek.myfarmer.ui.core.appstate.AppUiController
import com.tondracek.myfarmer.ui.core.appstate.LocalAppUiController
import com.tondracek.myfarmer.ui.core.navigation.NavGraph
import com.tondracek.myfarmer.ui.core.navigation.isInNavGraph
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.core.uievents.MyFarmerSnackBar
import com.tondracek.myfarmer.ui.core.uievents.SnackBarType
import com.tondracek.myfarmer.ui.core.util.toUserFriendlyMessage

@Composable
fun AppScaffold(
    navController: NavController,
    appUiController: AppUiController,
    content: @Composable BoxScope.() -> Unit,
) {
    val context = LocalContext.current

    val navBarViewModel: NavBarViewModel = hiltViewModel()
    val navBarState by navBarViewModel.state.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        appUiController.errors.collect { message ->
            snackbarHostState.showSnackbar(
                message = message.toUserFriendlyMessage(context),
                actionLabel = SnackBarType.ERROR,
                duration = SnackbarDuration.Short,
            )
        }
    }
    LaunchedEffect(Unit) {
        appUiController.errorMessages.collect { message ->
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = SnackBarType.ERROR,
                duration = SnackbarDuration.Short,
            )
        }
    }
    LaunchedEffect(Unit) {
        appUiController.successMessages.collect { message ->
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = SnackBarType.SUCCESS,
                duration = SnackbarDuration.Short,
            )
        }
    }

    val showBottomBar = navController.isInNavGraph(NavGraph.MainFlow)

    CompositionLocalProvider(
        LocalAppUiController provides appUiController,
    ) {
        Scaffold(
            containerColor = MyFarmerTheme.colors.surface,
            bottomBar = {
                if (showBottomBar)
                    BottomNavigationBar(
                        state = navBarState,
                        navController = navController,
                    )
            },
            snackbarHost = {
                SnackbarHost(
                    modifier = Modifier.padding(16.dp),
                    hostState = snackbarHostState,
                    snackbar = { MyFarmerSnackBar(snackbarData = it) }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                content()
            }
        }
    }
}
