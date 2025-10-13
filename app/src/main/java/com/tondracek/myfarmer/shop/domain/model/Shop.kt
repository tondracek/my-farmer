package com.tondracek.myfarmer.shop.domain.model

import com.tondracek.myfarmer.common.ImageResource
import com.tondracek.myfarmer.productmenu.ProductMenu
import com.tondracek.myfarmer.shopcategory.ShopCategory

data class Shop(
    val name: String,
    val images: List<ImageResource>,
    val description: String,
    val categories: List<ShopCategory>,
    val menu: ProductMenu,
    val userRatings: List<UserRating>,
    val averageRating: Double,
    val location: ShopLocation,
    val id: ShopId,
)

typealias ShopId = String
