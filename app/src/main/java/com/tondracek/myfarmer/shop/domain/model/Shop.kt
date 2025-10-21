package com.tondracek.myfarmer.shop.domain.model

import com.tondracek.myfarmer.common.ImageResource
import com.tondracek.myfarmer.openinghours.domain.model.ShopOpeningHours
import com.tondracek.myfarmer.productmenu.ProductMenu
import com.tondracek.myfarmer.shopcategory.ShopCategory
import com.tondracek.myfarmer.shopreview.domain.model.ShopReview
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import java.util.UUID

typealias ShopId = UUID

data class Shop(
    val id: ShopId,
    val name: String?,
    val description: String?,
    val owner: SystemUser,
    val categories: List<ShopCategory>,
    val images: List<ImageResource>,
    val menu: ProductMenu,
    val location: ShopLocation,
    val openingHours: ShopOpeningHours,
    val reviews: List<ShopReview>,
)
