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
        this.value.toDouble().compareTo(other.value.toDouble())
}

val Number.km get() = Distance(this)


enum class DistanceUnit() {
    KM()
    ;

    @Composable
    fun toStringTranslated(): String = when (this) {
        KM -> stringResource(R.string.km)
    }
}