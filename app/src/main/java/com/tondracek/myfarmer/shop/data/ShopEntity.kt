package com.tondracek.myfarmer.shop.data

import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.core.repository.firestore.FirestoreCollectionName
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
@FirestoreCollectionName("shop")
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

@Singleton
class ShopMapper @Inject constructor() : EntityMapper<Shop, ShopEntity> {

    override fun toEntity(model: Shop) = ShopEntity(
        id = model.id.toString(),
        name = model.name,
        description = model.description,
        ownerId = model.ownerId.toString(),
        categories = model.categories.map { it.toEntity() },
        images = model.images.map { it.uri },
        menu = model.menu.toEntity(),
        location = model.location.toEntity(),
        openingHours = model.openingHours.toEntity(),
    )

    override fun toModel(entity: ShopEntity) = Shop(
        id = UUID.fromString(entity.id),
        name = entity.name,
        description = entity.description,
        ownerId = UUID.fromString(entity.ownerId),
        categories = entity.categories.map { it.toModel() },
        images = entity.images.map { ImageResource(it) },
        menu = entity.menu.toModel(),
        location = entity.location.toModel(),
        openingHours = entity.openingHours.toModel(),
    )
}