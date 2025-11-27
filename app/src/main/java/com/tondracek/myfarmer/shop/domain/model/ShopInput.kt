package com.tondracek.myfarmer.shop.domain.model

import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.openinghours.domain.model.OpeningHours
import com.tondracek.myfarmer.productmenu.domain.model.ProductMenu
import com.tondracek.myfarmer.shop.domain.result.MissingShopInputDataUCResult
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import com.tondracek.myfarmer.shoplocation.domain.model.ShopLocation
import java.util.UUID

data class ShopInput(
    val name: String = "",
    val description: String = "",
    val categories: List<ShopCategory> = emptyList(),
    val images: List<ImageResource> = emptyList(),
    val menu: ProductMenu? = null,
    val location: ShopLocation? = null,
    val openingHours: OpeningHours? = null,
)

fun ShopInput.toShop(
    shopId: ShopId,
    ownerId: UUID,
): UCResult<Shop> {
    if (this.menu == null || this.location == null || this.openingHours == null)
        return MissingShopInputDataUCResult

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
        name = this.name ?: "",
        description = this.description ?: "",
        categories = this.categories,
        images = this.images,
        menu = this.menu,
        location = this.location,
        openingHours = this.openingHours,
    )