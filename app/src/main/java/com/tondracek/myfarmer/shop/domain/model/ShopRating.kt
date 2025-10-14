package com.tondracek.myfarmer.shop.domain.model

import androidx.annotation.FloatRange

data class UserRating(
    @get:FloatRange(from = 0.0, to = 5.0)
    val stars: Double,
    val comment: String
)
