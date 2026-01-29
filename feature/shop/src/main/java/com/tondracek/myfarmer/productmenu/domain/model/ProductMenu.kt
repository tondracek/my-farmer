package com.tondracek.myfarmer.productmenu.domain.model

import java.util.UUID

data class ProductMenu(
    val items: List<MenuItem>
) {
    companion object {
        val Empty = ProductMenu(emptyList())
    }
}

@JvmInline
value class MenuItemId(val value: UUID) {
    companion object {
        fun new() = MenuItemId(UUID.randomUUID())

        fun fromString(value: String) = runCatching {
            MenuItemId(UUID.fromString(value))
        }.getOrDefault(null)
    }

    override fun toString(): String = value.toString()
}

data class MenuItem(
    val id: MenuItemId = MenuItemId.new(),
    val name: String,
    val description: String,
    val price: PriceLabel,
    val inStock: Boolean,
)

data class PriceLabel(
    val value: String,
) {
    override fun toString(): String = value
}
