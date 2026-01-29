package com.tondracek.myfarmer.ui.common.distance

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.location.domain.model.Distance
import com.tondracek.myfarmer.location.domain.model.DistanceUnit
import com.tondracek.myfarmer.location.domain.model.DistanceUnit.KM
import com.tondracek.myfarmer.location.domain.model.DistanceUnit.M
import java.lang.String.format
import java.text.DecimalFormatSymbols

@Composable
fun Distance.toStringTranslated(): String {
    val locale = Locale.current.platformLocale

    val formatted = format(locale, "%.2f", value.toDouble())

    val decimalSeparator = DecimalFormatSymbols.getInstance(locale).decimalSeparator
    val escapedSeparator = Regex.escape(decimalSeparator.toString())

    val trimmed = formatted
        .replace(Regex("0+$"), "")
        .replace(Regex("$escapedSeparator$"), "")

    return "$trimmed ${unit.toStringTranslated()}"
}

@Composable
fun DistanceUnit.toStringTranslated(): String = when (this) {
    KM -> stringResource(R.string.km)
    M -> "m"
}