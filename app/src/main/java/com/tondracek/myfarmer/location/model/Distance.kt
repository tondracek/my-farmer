package com.tondracek.myfarmer.location.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import com.tondracek.myfarmer.R
import java.text.DecimalFormatSymbols

/**
 * Custom My Farmer distance data class
 */
data class Distance(
    val value: Number,
    val unit: DistanceUnit = DistanceUnit.KM,
) : Comparable<Distance> {

    fun toMeters(): Double = when (unit) {
        DistanceUnit.KM -> value.toDouble() * 1000.0
        DistanceUnit.M -> value.toDouble()
    }

    @Composable
    fun toStringTranslated(): String {
        val locale = Locale.current.platformLocale

        val formatted = String.format(locale, "%.2f", value.toDouble())

        val decimalSeparator = DecimalFormatSymbols.getInstance(locale).decimalSeparator
        val escapedSeparator = Regex.escape(decimalSeparator.toString())

        val trimmed = formatted
            .replace(Regex("0+$"), "")
            .replace(Regex("$escapedSeparator$"), "")

        return "$trimmed ${unit.toStringTranslated()}"
    }

    override fun compareTo(other: Distance): Int =
        this.toMeters().compareTo(other.toMeters())
}

val Number.km get() = Distance(this)
val Number.meters get() = Distance(this, DistanceUnit.M)


enum class DistanceUnit() {
    KM(),
    M(),
    ;

    @Composable
    fun toStringTranslated(): String = when (this) {
        KM -> stringResource(R.string.km)
        M -> "m"
    }
}