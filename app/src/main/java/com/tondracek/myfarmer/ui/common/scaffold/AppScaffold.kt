package com.tondracek.myfarmer.ui.common.scaffold

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.tondracek.myfarmer.ui.common.navbar.BottomNavigationBar
import com.tondracek.myfarmer.ui.common.navbar.NavBarState
import com.tondracek.myfarmer.ui.common.navbar.NavBarViewModel
import com.tondracek.myfarmer.ui.core.appstate.AppUiController
import com.tondracek.myfarmer.ui.core.appstate.LocalAppUiController
import com.tondracek.myfarmer.ui.core.navigation.NavGraph
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.core.navigation.getCurrentRoute
import com.tondracek.myfarmer.ui.core.navigation.isInNavGraph
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.core.uievents.MyFarmerSnackBar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Composable
fun AppScaffold(
    navController: NavController,
    content: @Composable BoxScope.() -> Unit,
) {
    val navBarViewModel: NavBarViewModel = hiltViewModel()
    val navBarState by getNavBarState(
        isLoggedIn = navBarViewModel.isLoggedIn,
        currentRoute = navController.getCurrentRoute()
    ).collectAsState(NavBarState())

    val appUiController = remember { AppUiController() }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        appUiController.errors.collect { errorMessage ->
            snackbarHostState.showSnackbar(errorMessage)
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
                        onNavigate = { route -> navController.navigate(route) }
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

@Composable
private fun getNavBarState(
    isLoggedIn: Flow<Boolean>,
    currentRoute: Flow<Route?>,
) = combine(
    isLoggedIn,
    currentRoute,
) { loggedIn, route ->
    NavBarState(
        isLoggedIn = loggedIn,
        currentRoute = route,
    )
}
