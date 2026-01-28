package com.tondracek.myfarmer.location.domain.model

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

    override fun compareTo(other: Distance): Int =
        this.toMeters().compareTo(other.toMeters())
}

val Number.km get() = Distance(this)
val Number.meters get() = Distance(this, DistanceUnit.M)


enum class DistanceUnit {
    KM,
    M,
    ;
}