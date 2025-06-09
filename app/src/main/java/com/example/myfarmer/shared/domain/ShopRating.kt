package com.example.myfarmer.shared.domain

import androidx.annotation.FloatRange

data class UserRating(
    @FloatRange(from = 0.0, to = 5.0)
    val stars: Double,
    val comment: String
)
