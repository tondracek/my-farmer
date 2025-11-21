package com.tondracek.myfarmer.shop.domain.model

import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.openinghours.domain.model.OpeningHours
import com.tondracek.myfarmer.productmenu.domain.model.ProductMenu
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import com.tondracek.myfarmer.shoplocation.domain.model.ShopLocation

data class ShopInput(
    val name: String = "",
    val description: String = "",
    val categories: List<ShopCategory> = emptyList(),
    val images: List<ImageResource> = emptyList(),
    val menu: ProductMenu? = null,
    val location: ShopLocation? = null,
    val openingHours: OpeningHours? = null,
)