package com.tondracek.myfarmer.shop.domain.model

import java.util.UUID

@JvmInline
value class ShopId(val value: UUID) {

    override fun toString() = value.toString()

    companion object {

        fun fromString(id: String) = ShopId(UUID.fromString(id))

        fun newId() = ShopId(UUID.randomUUID())
    }
}