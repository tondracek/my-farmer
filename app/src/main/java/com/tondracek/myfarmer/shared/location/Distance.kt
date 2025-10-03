package com.tondracek.myfarmer.shared.location

data class Distance(
    val value: Number,
) {
    override fun toString() = "$value km"
}

val Number.km get() = Distance(this)