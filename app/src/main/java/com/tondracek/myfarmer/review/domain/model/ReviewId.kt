package com.tondracek.myfarmer.review.domain.model

import java.util.UUID

@JvmInline
value class ReviewId(val value: UUID) {
    override fun toString() = value.toString()

    companion object {

        fun fromString(id: String) = ReviewId(UUID.fromString(id))

        fun newId() = ReviewId(UUID.randomUUID())
    }
}