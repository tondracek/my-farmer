package com.tondracek.myfarmer.shop.domain.model

import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.core.domain.domainerror.InputDataError
import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult
import com.tondracek.myfarmer.location.model.Location
import com.tondracek.myfarmer.openinghours.domain.model.OpeningHours
import com.tondracek.myfarmer.productmenu.domain.model.ProductMenu
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import com.tondracek.myfarmer.systemuser.domain.model.UserId

data class ShopInput(
    val name: String = "",
    val description: String = "",
    val categories: List<ShopCategory> = emptyList(),
    val images: List<ImageResource> = emptyList(),
    val menu: ProductMenu = ProductMenu.Empty,
    val location: Location? = null,
    val openingHours: OpeningHours = OpeningHours.Empty,
)

fun ShopInput.toShop(
    shopId: ShopId,
    ownerId: UserId,
): UCResult<Shop> {
    if (this.location == null)
        return UCResult.Failure(InputDataError.MissingLocationInput)

    return Shop(
        id = shopId,
        name = this.name,
        description = this.description,
        ownerId = ownerId,
        categories = this.categories,
        images = this.images,
        menu = this.menu,
        location = this.location,
        openingHours = this.openingHours,
    ).let { UCResult.Success(it) }
}

fun Shop.toShopInput(): ShopInput =
    ShopInput(
        name = this.name,
        description = this.description ?: "",
        categories = this.categories,
        images = this.images,
        menu = this.menu,
        location = this.location,
        openingHours = this.openingHours,
    )