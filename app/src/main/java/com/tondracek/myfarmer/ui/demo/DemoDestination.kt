package com.tondracek.myfarmer.ui.demo

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object DemoScreenRoute

fun NavGraphBuilder.demoDestination() {
    composable<DemoScreenRoute> {
        val viewmodel: DemoViewmodel = hiltViewModel()

        val state by viewmodel.state.collectAsState()

        DemoScreen(
            state = state,
            onAddDemoClick = viewmodel::addDemo,
        )
    }
}