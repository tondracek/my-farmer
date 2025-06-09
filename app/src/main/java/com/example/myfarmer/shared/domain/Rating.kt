package com.example.myfarmer.shared.domain

import java.time.LocalDateTime

data class Rating(
    val rating: Int,
    val comment: String,
    val author: String,
    val date: LocalDateTime,
)