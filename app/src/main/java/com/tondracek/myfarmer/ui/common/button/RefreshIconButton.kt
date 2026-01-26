package com.tondracek.myfarmer.ui.common.button

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate

@Composable
fun RefreshIconButton(
    isRefreshing: Boolean = false,
    onClick: () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (isRefreshing) 360f else 0f,
        animationSpec = when {
            isRefreshing -> infiniteRepeatable(
                animation = tween(durationMillis = 1000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )

            else -> tween(durationMillis = 0)
        },
        label = "refreshRotation"
    )

    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Refresh icon",
            modifier = Modifier.rotate(rotation)
        )
    }
}
