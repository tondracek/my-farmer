package com.tondracek.myfarmer.shopcategory.domain.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.serialization.Serializable

data class ShopCategory(
    val name: String,
    val color: Color
)

@Serializable
data class ShopCategorySerializable(
    val name: String,
    val color: Int
)

fun ShopCategory.toSerializable(): ShopCategorySerializable =
    ShopCategorySerializable(
        name = name,
        color = color.toArgb()
    )

fun ShopCategorySerializable.toDomain(): ShopCategory =
    ShopCategory(
        name = name,
        color = Color(color)
    )