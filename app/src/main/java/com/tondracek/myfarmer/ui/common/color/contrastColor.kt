package com.tondracek.myfarmer.ui.common.color

import androidx.compose.ui.graphics.Color

fun contrastColor(color: Color): Color {
    val luminance = (0.299 * color.red + 0.587 * color.green + 0.114 * color.blue)
    return if (luminance > 0.5) Color.Black else Color.White
}