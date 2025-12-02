package com.tondracek.myfarmer.location.model

import android.content.Context
import com.tondracek.myfarmer.R
import java.util.Locale

/**
 * Custom My Farmer distance data class
 */
data class Distance(
    val value: Number,
    val unit: DistanceUnit = DistanceUnit.KM,
) : Comparable<Distance> {

    override fun toString(): String {
        val formatted = String.format(Locale.US, "%.2f", value.toDouble())
        val trimmed = formatted
            .replace(Regex("0+$"), "")   // remove trailing zeros
            .replace(Regex("\\.$"), "")  // remove trailing decimal point
        return "$trimmed $unit"
    }

    override fun compareTo(other: Distance): Int =
        this.value.toDouble().compareTo(other.value.toDouble())
}

val Number.km get() = Distance(this)


enum class DistanceUnit() {
    KM()
    ;

    fun toString(context: Context) = when (this) {
        KM -> context.getString(R.string.km)
    }
}