package com.tondracek.myfarmer.ui.shopslistview.components

import com.tondracek.myfarmer.common.ImageResource
import com.tondracek.myfarmer.location.model.Distance
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory

data class ShopListViewItem(
    val id: ShopId,
    val name: String?,
    val description: String?,
    val categories: List<ShopCategory>,
    val image: ImageResource?,
    val distance: Distance?,
    val averageRating: Double,
)

fun Shop.toListItem(distance: Distance?) = ShopListViewItem(
    id = id,
    image = images.firstOrNull(),
    name = name,
    categories = categories,
    distance = distance,
    averageRating = 0.0, // TODO
    description = description,
)
