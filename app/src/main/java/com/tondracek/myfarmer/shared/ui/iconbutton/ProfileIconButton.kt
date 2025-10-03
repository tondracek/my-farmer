package com.tondracek.myfarmer.shared.ui.iconbutton

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun ProfileIconButton(onProfileClick: () -> Unit) {
    IconButton(onClick = onProfileClick) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Profile"
        )
    }
}