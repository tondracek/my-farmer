package com.tondracek.myfarmer.ui.common.color

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.tondracek.myfarmer.common.color.RgbColor
import com.tondracek.myfarmer.common.color.fromArgb
import com.tondracek.myfarmer.common.color.toArgb

fun Color.toRgbColor(): RgbColor =
    RgbColor.fromArgb(this.toArgb())

fun RgbColor.toColor(): Color =
    Color(this.toArgb())