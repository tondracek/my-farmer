package com.tondracek.myfarmer.shop.data.entity

import com.tondracek.myfarmer.core.data.firestore.FirestoreEntity
import com.tondracek.myfarmer.image.model.ImageResource
import com.tondracek.myfarmer.location.data.entity.LocationEntity
import com.tondracek.myfarmer.location.data.entity.toEntity
import com.tondracek.myfarmer.location.data.entity.toModel
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shopcategory.data.ShopCategoryEntity
import com.tondracek.myfarmer.shopcategory.data.toEntity
import com.tondracek.myfarmer.shopcategory.data.toModel
import com.tondracek.myfarmer.user.domain.model.UserId
import kotlinx.serialization.Serializable

@Serializable
data class ShopEntity(
    override var id: String = "",
    var name: String = "",
    var description: String = "",
    var ownerId: String = "",
    var categories: List<ShopCategoryEntity> = emptyList(),
    var images: List<String?> = emptyList(),
    var menu: ProductMenuEntity = ProductMenuEntity(),
    var location: LocationEntity = LocationEntity(),
    var openingHours: OpeningHoursEntity = OpeningHoursEntity(),
) : FirestoreEntity

fun Shop.toEntity() = ShopEntity(
    id = id.toString(),
    name = name,
    description = description,
    ownerId = ownerId.toString(),
    categories = categories.map { it.toEntity() },
    images = images.map { it.uri },
    menu = menu.toEntity(),
    location = location.toEntity(),
    openingHours = openingHours.toEntity(),
)

fun ShopEntity.toModel() = Shop(
    id = ShopId.fromString(id),
    name = name,
    description = description,
    ownerId = UserId.fromString(ownerId),
    categories = categories.map { it.toModel() },
    images = images.map { ImageResource(it) },
    menu = menu.toModel(),
    location = location.toModel(),
    openingHours = openingHours.toModel(),
)