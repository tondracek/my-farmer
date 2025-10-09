package com.tondracek.myfarmer.ui.shopslistview.components

import com.tondracek.myfarmer.common.ImageResource
import com.tondracek.myfarmer.location.model.Distance
import com.tondracek.myfarmer.productcategory.ProductCategory
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId

data class ShopListViewItem(
    val id: ShopId,
    val picture: ImageResource?,
    val name: String,
    val categories: List<ProductCategory>,
    val distance: Distance?,
    val rating: Double,
    val description: String,
)

fun Shop.toListItem(distance: Distance?) = ShopListViewItem(
    id = id,
    picture = images.firstOrNull(),
    name = name,
    categories = categories,
    distance = distance,
    rating = averageRating,
    description = description,
)