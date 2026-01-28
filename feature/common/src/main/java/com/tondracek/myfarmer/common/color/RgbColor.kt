package com.tondracek.myfarmer.common.color

data class RgbColor(
    val red: Short,
    val green: Short,
    val blue: Short
) {
    companion object {
        val White = RgbColor(255, 255, 255)
    }
}

fun RgbColor.toArgb(): Int =
    (0xFF shl 24) or
            ((red.toInt() and 0xFF) shl 16) or
            ((green.toInt() and 0xFF) shl 8) or
            (blue.toInt() and 0xFF)

fun RgbColor.Companion.fromArgb(argb: Int) = RgbColor(
    red = ((argb shr 16) and 0xFF).toShort(),
    green = ((argb shr 8) and 0xFF).toShort(),
    blue = (argb and 0xFF).toShort()
)