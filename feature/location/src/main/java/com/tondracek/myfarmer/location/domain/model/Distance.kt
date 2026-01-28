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

    operator fun minus(other: Distance): Distance {
        val resultMeters = this.toMeters() - other.toMeters()

        return when (unit) {
            DistanceUnit.KM -> Distance(resultMeters / 1000.0, DistanceUnit.KM)
            DistanceUnit.M -> Distance(resultMeters, DistanceUnit.M)
        }
    }
}

val Number.km get() = Distance(this)
val Number.meters get() = Distance(this, DistanceUnit.M)


enum class DistanceUnit {
    KM,
    M,
    ;
}