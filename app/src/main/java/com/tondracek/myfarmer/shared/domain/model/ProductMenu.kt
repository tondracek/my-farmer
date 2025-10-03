package com.tondracek.myfarmer.shared.domain.model

data class ProductMenu(
    val products: List<Product>
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
