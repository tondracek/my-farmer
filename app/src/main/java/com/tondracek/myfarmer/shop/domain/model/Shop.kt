package com.tondracek.myfarmer.shop.domain.model

import com.tondracek.myfarmer.common.ImageResource
import com.tondracek.myfarmer.productcategory.ProductCategory
import com.tondracek.myfarmer.productmenu.ProductMenu

data class Shop(
    val name: String,
    val images: List<ImageResource>,
    val description: String,
    val categories: List<ProductCategory>,
    val menu: ProductMenu,
    val userRatings: List<UserRating>,
    val averageRating: Double,
    val location: ShopLocation,
    val id: ShopId,
)

typealias ShopId = String
