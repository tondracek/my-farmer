package com.tondracek.myfarmer.shop.data

import com.tondracek.myfarmer.core.repository.fake.FakeRepositoryCore

fun getFakeShopRepository() = ShopRepository(
    core = FakeRepositoryCore(),
    mapper = ShopMapper()
)