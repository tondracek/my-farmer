package com.tondracek.myfarmer.shopcategory.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import kotlinx.serialization.Serializable

@Serializable
data class ShopCategoryEntity(
    var name: String = "",
    var colorArgb: Int = Color.White.toArgb()
)

fun ShopCategoryEntity.toModel() = ShopCategory(
    name = name,
    color = Color(colorArgb)
)

fun ShopCategory.toEntity() = ShopCategoryEntity(
    name = name,
    colorArgb = color.toArgb()
)
