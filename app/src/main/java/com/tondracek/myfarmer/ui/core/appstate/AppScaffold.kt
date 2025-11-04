package com.tondracek.myfarmer.ui.core.appstate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.common.navbar.BottomNavigationBar
import com.tondracek.myfarmer.ui.common.navbar.NavBarViewModel
import com.tondracek.myfarmer.ui.common.topbar.FloatingTopBar
import com.tondracek.myfarmer.ui.core.navigation.AppNavigator
import com.tondracek.myfarmer.ui.core.navigation.LocalAppNavigator
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    appNavigator: AppNavigator,
    content: @Composable BoxScope.() -> Unit,
) {
    val navBarViewModel: NavBarViewModel = hiltViewModel()
    val navBarState by navBarViewModel.state.collectAsState()

    var appUiState by remember { mutableStateOf(AppUiState(title = "MyFarmer")) }
    val appUiController = remember {
        object : AppUiController {
            override fun updateTitle(title: String) {
                appUiState = appUiState.copy(title = title)
            }
        }
    }

    CompositionLocalProvider(
        LocalAppUiState provides appUiState,
        LocalAppUiController provides appUiController,
        LocalAppNavigator provides appNavigator,
    ) {
        Scaffold(
            modifier = modifier,
            containerColor = MyFarmerTheme.colors.surfaceContainer,
            bottomBar = {
                BottomNavigationBar(
                    state = navBarState,
                    onItemSelected = navBarViewModel::onItemSelected,
                    onNavigate = navBarViewModel::onNavigate
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

                FloatingTopBar(
                    modifier = Modifier.align(Alignment.TopCenter),
                    title = appUiState.title ?: stringResource(R.string.app_name)
                )
            }
        }
    }
}