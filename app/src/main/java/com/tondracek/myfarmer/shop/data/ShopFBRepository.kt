package com.tondracek.myfarmer.shop.data

import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.core.repository.firebase.FirebaseRepository
import com.tondracek.myfarmer.shop.domain.model.Shop

class ShopFBRepository() : ShopRepository,
    FirebaseRepository<Shop, ShopEntity>(ShopEntity::class.java) {

    override val mapper: EntityMapper<Shop, ShopEntity> = object : EntityMapper<Shop, ShopEntity> {
        override fun toEntity(model: Shop): ShopEntity = model.toEntity()
        override fun toModel(entity: ShopEntity): Shop = entity.toModel()
    }
}
