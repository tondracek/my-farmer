package com.tondracek.myfarmer.shared.domain.model

import androidx.annotation.FloatRange

data class UserRating(
    @FloatRange(from = 0.0, to = 5.0)
    val stars: Double,
    val comment: String
)
