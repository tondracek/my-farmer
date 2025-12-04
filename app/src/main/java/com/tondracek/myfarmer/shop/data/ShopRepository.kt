package com.tondracek.myfarmer.shop.data

import com.tondracek.myfarmer.core.repository.BaseRepository
import com.tondracek.myfarmer.core.repository.RepositoryCoreFactory
import com.tondracek.myfarmer.shop.domain.model.Shop
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShopRepository @Inject constructor(
    mapper: ShopMapper,
    factory: RepositoryCoreFactory,
) : BaseRepository<Shop>(factory.create(mapper, ShopEntity::class.java))
