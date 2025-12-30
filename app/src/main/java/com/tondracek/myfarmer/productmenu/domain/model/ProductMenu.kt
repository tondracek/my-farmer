package com.tondracek.myfarmer.productmenu.domain.model

data class ProductMenu(
    val items: List<MenuItem>
) {
    companion object {
        val Empty = ProductMenu(emptyList())
    }
}

data class MenuItem(
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
