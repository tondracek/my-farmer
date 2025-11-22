package com.tondracek.myfarmer.ui.core.appstate

import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.common.navbar.BottomNavigationBar
import com.tondracek.myfarmer.ui.common.navbar.NavBarViewModel
import com.tondracek.myfarmer.ui.common.topbar.FloatingTopBar
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val navBarViewModel: NavBarViewModel = hiltViewModel()
    val navBarState by navBarViewModel.state.collectAsState()

    var appUiState by remember { mutableStateOf(AppUiState()) }
    val appUiController = remember {
        object : AppUiController {
            override fun updateTitle(title: String) = apply {
                appUiState = appUiState.copy(title = title)
            }

            override fun updateTopBarPadding(applyPadding: Boolean) = apply {
                appUiState = appUiState.copy(applyTopBarPadding = applyPadding)
            }
        }
    }

    val localDensity = LocalDensity.current

    CompositionLocalProvider(
        LocalAppUiState provides appUiState,
        LocalAppUiController provides appUiController,
    ) {
        Scaffold(
            modifier = modifier,
            containerColor = MyFarmerTheme.colors.surfaceContainer,
            bottomBar = {
                BottomNavigationBar(
                    state = navBarState,
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
                var topContentPadding by remember { mutableStateOf(0.dp) }
                val animatedTopContentPadding by animateDpAsState(
                    if (appUiState.applyTopBarPadding) topContentPadding else 0.dp
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = animatedTopContentPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    content()
                }

                FloatingTopBar(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .onGloballyPositioned { coordinates ->
                            with(localDensity) {
                                topContentPadding = coordinates.size.height.toDp()
                            }
                        },
                    title = appUiState.title ?: stringResource(R.string.app_name)
                )
            }
        }
    }
}