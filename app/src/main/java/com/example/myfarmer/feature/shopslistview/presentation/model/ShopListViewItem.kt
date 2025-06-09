package com.example.myfarmer.feature.shopslistview.presentation.model

import com.example.myfarmer.shared.domain.Category
import com.example.myfarmer.shared.domain.ImageResource
import com.example.myfarmer.shared.domain.Shop
import com.example.myfarmer.shared.domain.ShopId

data class ShopListViewItem(
    val id: ShopId,
    val picture: ImageResource?,
    val name: String,
    val categories: List<Category>,
    val distance: String?,
    val rating: Double,
    val description: String,
)

fun Shop.toListItem() = ShopListViewItem(
    id = id,
    picture = images.firstOrNull(),
    name = name,
    categories = categories,
    distance = "2.5 km",
    rating = averageRating,
    description = description,
)