package com.tondracek.myfarmer.shopcategory.data

import com.tondracek.myfarmer.common.color.RgbColor
import com.tondracek.myfarmer.common.color.fromArgb
import com.tondracek.myfarmer.common.color.toArgb
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import kotlinx.serialization.Serializable

@Serializable
data class ShopCategoryEntity(
    var name: String = "",
    var colorArgb: Int = 0xFFFFFFFF.toInt()
)

fun ShopCategoryEntity.toModel() = ShopCategory(
    name = name,
    color = RgbColor.fromArgb(colorArgb)
)

fun ShopCategory.toEntity() = ShopCategoryEntity(
    name = normalizeWord(name),
    colorArgb = color.toArgb()
)

fun normalizeWord(input: String): String {
    if (input.isEmpty()) return input
    return input.take(1).uppercase() + input.substring(1).lowercase()
}
