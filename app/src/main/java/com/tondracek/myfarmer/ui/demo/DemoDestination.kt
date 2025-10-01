package com.tondracek.myfarmer.ui.demo

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tondracek.myfarmer.ui.shopscreen.presentation.ShopsScreenRoute
import kotlinx.serialization.Serializable

@Serializable
data object DemoScreenRoute

fun NavGraphBuilder.demoDestination() {
    composable<ShopsScreenRoute> {
        val viewmodel: DemoViewmodel = viewModel()

        val state by viewmodel.state.collectAsState()

        DemoScreen(
            items = state,
            onAddDemoClick = viewmodel::addDemo,
        )
    }
}