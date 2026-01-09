package com.tondracek.myfarmer.systemuser.domain.model

import java.util.UUID

@JvmInline
value class UserId(val value: UUID) {

    override fun toString() = value.toString()

    companion object {

        fun fromString(id: String) = UserId(UUID.fromString(id))

        fun newId() = UserId(UUID.randomUUID())
    }
}