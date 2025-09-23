package com.tondracek.myfarmer.feature.shopslistview.presentation.model

import com.tondracek.myfarmer.shared.domain.model.Category
import com.tondracek.myfarmer.shared.domain.model.ImageResource
import com.tondracek.myfarmer.shared.domain.model.Shop
import com.tondracek.myfarmer.shared.domain.model.ShopId
import com.tondracek.myfarmer.shared.location.Distance

data class ShopListViewItem(
    val id: ShopId,
    val picture: ImageResource?,
    val name: String,
    val categories: List<Category>,
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