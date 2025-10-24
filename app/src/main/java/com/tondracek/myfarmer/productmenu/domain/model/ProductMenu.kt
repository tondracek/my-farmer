package com.tondracek.myfarmer.productmenu.domain.model

data class ProductMenu(
    val items: List<MenuItem>
)

data class MenuItem(
    val name: String,
    val description: String?,
    val price: ProductPrice,
    val inStock: Boolean,
)

data class ProductPrice(
    val value: String,
)
