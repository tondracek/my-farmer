package com.tondracek.myfarmer.shared.ui.topbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CustomTopAppBar(
    title: String,
    navigationIcon: @Composable () -> Unit,
    actions: @Composable (RowScope.() -> Unit),
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = navigationIcon,
        actions = actions,
        colors = customTopBarColors(),
    )
}