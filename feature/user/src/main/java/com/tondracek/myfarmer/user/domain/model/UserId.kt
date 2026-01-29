package com.tondracek.myfarmer.user.domain.model

import java.util.UUID

@JvmInline
value class UserId private constructor(val value: UUID) {

    override fun toString() = value.toString()

    companion object {

        fun fromString(id: String) = UserId(UUID.fromString(id))

        fun newId() = UserId(UUID.randomUUID())
    }
}