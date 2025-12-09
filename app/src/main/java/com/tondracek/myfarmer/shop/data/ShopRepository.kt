package com.tondracek.myfarmer.shop.data

import com.tondracek.myfarmer.core.repository.BaseRepository
import com.tondracek.myfarmer.core.repository.RepositoryCore
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntityId
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShopRepository @Inject constructor(
    core: RepositoryCore<ShopEntity, FirestoreEntityId>,
    mapper: ShopMapper,
) : BaseRepository<Shop, ShopId, ShopEntity, FirestoreEntityId>(
    core = core,
    mapper = mapper
)
