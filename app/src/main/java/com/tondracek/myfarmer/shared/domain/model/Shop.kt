package com.tondracek.myfarmer.shared.domain.model

data class Shop(
    val name: String,
    val images: List<ImageResource>,
    val description: String,
    val categories: List<Category>,
    val menu: ProductMenu,
    val userRatings: List<UserRating>,
    val averageRating: Double,
    val location: ShopLocation,
    val id: ShopId,
)

typealias ShopId = String
