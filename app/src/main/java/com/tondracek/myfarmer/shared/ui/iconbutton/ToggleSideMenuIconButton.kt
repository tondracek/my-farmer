package com.tondracek.myfarmer.shared.ui.iconbutton

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun ToggleSideMenuIconButton(onToggleSideMenu: () -> Unit) {
    IconButton(onClick = onToggleSideMenu) {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Toggle Side Menu"
        )
    }
}