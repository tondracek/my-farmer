package com.tondracek.myfarmer.shop.data

import com.tondracek.myfarmer.core.repository.fake.FakeRepositoryCoreFactoryFirestoreEntity

fun getFakeShopRepository() = ShopRepository(
    core = FakeRepositoryCoreFactoryFirestoreEntity<ShopEntity>().create(
        mapper = ShopMapper(),
        entityClass = ShopEntity::class.java,
    )
)