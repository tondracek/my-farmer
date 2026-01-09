package com.tondracek.myfarmer.shop.domain.model

import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.openinghours.domain.model.OpeningHours
import com.tondracek.myfarmer.productmenu.domain.model.ProductMenu
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import com.tondracek.myfarmer.shoplocation.domain.model.ShopLocation
import com.tondracek.myfarmer.systemuser.domain.model.UserId

data class Shop(
    val id: ShopId,
    val name: String?,
    val description: String?,
    val ownerId: UserId,
    val categories: List<ShopCategory>,
    val images: List<ImageResource>,
    val menu: ProductMenu,
    val location: ShopLocation,
    val openingHours: OpeningHours,
)
