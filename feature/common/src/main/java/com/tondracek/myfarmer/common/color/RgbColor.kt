package com.tondracek.myfarmer.common.color

data class RgbColor(
    val red: Short,
    val green: Short,
    val blue: Short
) {
    companion object {
        val Black = RgbColor(0, 0, 0)
        val White = RgbColor(255, 255, 255)
        val Red = RgbColor(255, 0, 0)
        val Green = RgbColor(0, 255, 0)
        val Blue = RgbColor(0, 0, 255)
    }
}

fun RgbColor.toArgb(): Int =
    (0xFF shl 24) or
            ((red.toInt() and 0xFF) shl 16) or
            ((green.toInt() and 0xFF) shl 8) or
            (blue.toInt() and 0xFF)

fun RgbColor.Companion.fromArgb(argb: Int): RgbColor =
    RgbColor(
        red = ((argb shr 16) and 0xFF).toShort(),
        green = ((argb shr 8) and 0xFF).toShort(),
        blue = (argb and 0xFF).toShort()
    )