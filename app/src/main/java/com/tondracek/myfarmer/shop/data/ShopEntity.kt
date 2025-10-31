package com.tondracek.myfarmer.shop.data

import com.tondracek.myfarmer.common.model.ImageResource
import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.core.repository.firestore.FirestoreCollection
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntity
import com.tondracek.myfarmer.openinghours.data.OpeningHoursEntity
import com.tondracek.myfarmer.openinghours.data.toEntity
import com.tondracek.myfarmer.openinghours.data.toModel
import com.tondracek.myfarmer.productmenu.data.ProductMenuEntity
import com.tondracek.myfarmer.productmenu.data.toEntity
import com.tondracek.myfarmer.productmenu.data.toModel
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shopcategory.data.ShopCategoryEntity
import com.tondracek.myfarmer.shopcategory.data.toEntity
import com.tondracek.myfarmer.shopcategory.data.toModel
import com.tondracek.myfarmer.shoplocation.data.ShopLocationEntity
import com.tondracek.myfarmer.shoplocation.data.toEntity
import com.tondracek.myfarmer.shoplocation.data.toModel
import kotlinx.serialization.Serializable
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
@FirestoreCollection("shop")
data class ShopEntity(
    override var id: String = "",
    var name: String? = null,
    var description: String? = null,
    var ownerId: String = "",
    var categories: List<ShopCategoryEntity> = emptyList(),
    var images: List<String?> = emptyList(),
    var menu: ProductMenuEntity = ProductMenuEntity(),
    var location: ShopLocationEntity = ShopLocationEntity(),
    var openingHours: OpeningHoursEntity = OpeningHoursEntity(),
) : FirestoreEntity

fun ShopEntity.toModel() = Shop(
    id = UUID.fromString(id),
    name = name,
    description = description,
    ownerId = UUID.fromString(ownerId),
    categories = categories.map { it.toModel() },
    images = images.map { ImageResource(it) },
    menu = menu.toModel(),
    location = location.toModel(),
    openingHours = openingHours.toModel(),
)

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

@Singleton
class ShopMapper @Inject constructor() : EntityMapper<Shop, ShopEntity> {
    override fun toModel(entity: ShopEntity): Shop = entity.toModel()
    override fun toEntity(model: Shop): ShopEntity = model.toEntity()
}