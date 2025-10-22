package com.tondracek.myfarmer.shop.data

import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.core.repository.firebase.FirebaseRepository
import com.tondracek.myfarmer.shop.domain.model.Shop

class ShopFBRepository(override val mapper: EntityMapper<Shop, ShopEntity>) : ShopRepository,
    FirebaseRepository<Shop, ShopEntity>(ShopEntity::class.java)


