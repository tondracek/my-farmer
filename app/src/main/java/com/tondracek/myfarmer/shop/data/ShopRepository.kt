package com.tondracek.myfarmer.shop.data

import com.tondracek.myfarmer.core.repository.BaseRepository
import com.tondracek.myfarmer.core.repository.IdMapper
import com.tondracek.myfarmer.core.repository.RepositoryCore
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntityId
import com.tondracek.myfarmer.core.repository.request.filterEq
import com.tondracek.myfarmer.core.repository.request.repositoryRequest
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.systemuser.domain.model.UserId
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShopRepository @Inject constructor(
    core: RepositoryCore<ShopEntity, FirestoreEntityId>,
    mapper: ShopMapper,
) : BaseRepository<Shop, ShopId, ShopEntity, FirestoreEntityId>(
    core = core,
    entityMapper = mapper,
    idMapper = object : IdMapper<ShopId, FirestoreEntityId> {
        override fun toEntityId(modelId: ShopId): FirestoreEntityId = modelId.toString()
        override fun toModelId(entityId: FirestoreEntityId): ShopId = ShopId.fromString(entityId)
    },
) {
    fun getByOwnerId(ownerId: UserId): Flow<List<Shop>> = get(
        repositoryRequest {
            addFilter(ShopEntity::ownerId filterEq ownerId.toString())
        }
    )
}
