package com.example.myfarmer.shared.domain

data class Shop(
    val name: String,
    val images: List<ImageResource>,
    val description: String,
    val categories: List<Category>,
    val menu: ProductMenu,
    val userRatings: List<UserRating>,
    val averageRating: Double,
    val location: ShopLocation,
    val id: ShopId = "", /* TODO: Use UUID or similar */
)

typealias ShopId = String
