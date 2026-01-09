package com.tondracek.myfarmer.ui.mainshopscreen.shopslistview.components

import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.location.model.Distance
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory

data class ShopListViewItem(
    val id: ShopId,
    val name: String?,
    val description: String?,
    val categories: List<ShopCategory>,
    val image: ImageResource,
    val distance: Distance?,
    val averageRating: Rating,
)

fun Shop.toListItem(distance: Distance?, averageRating: Rating) = ShopListViewItem(
    id = id,
    image = images.firstOrNull() ?: ImageResource.EMPTY,
    name = name,
    categories = categories,
    distance = distance,
    averageRating = averageRating,
    description = description,
)
