package com.tondracek.myfarmer.location.model

/**
 * Custom My Farmer distance data class
 */
data class Distance(
    val value: Number,
    val unit: DistanceUnit = DistanceUnit.KM,
) {
    override fun toString() = "$value km"
}

val Number.km get() = Distance(this)
