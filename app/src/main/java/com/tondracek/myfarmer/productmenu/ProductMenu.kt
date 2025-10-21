package com.tondracek.myfarmer.productmenu

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
    val price: String,
)

data class Product(
    val name: String,
    val pricePerUnit: PricePerUnit,
)

data class PricePerUnit(
    val price: String,
    val unitType: UnitType
)

enum class UnitType {
    KG,
    G,
    PIECE,
    LITER,
    ;
}
