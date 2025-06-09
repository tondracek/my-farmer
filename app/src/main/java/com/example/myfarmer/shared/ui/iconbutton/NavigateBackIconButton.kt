package com.example.myfarmer.shared.ui.iconbutton

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun NavigateBackIconButton(onNavigationIconClick: () -> Unit) {
    IconButton(onClick = onNavigationIconClick) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back"
        )
    }
}
