package com.tondracek.myfarmer.shop.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.tondracek.myfarmer.core.data.FirestoreCollectionNames
import com.tondracek.myfarmer.core.firestore.helpers.functions.firestoreGetPaginated
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
import kotlinx.coroutines.flow.map
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

    private val firestore = Firebase.firestore
    private val collection = firestore.collection(FirestoreCollectionNames.SHOP)

    fun getAll(limit: Int? = null, after: ShopId? = null): Flow<List<Shop>> =
        firestoreGetPaginated(
            collection = collection,
            entityClass = ShopEntity::class,
            limit = limit,
            after = after?.toString(),
        ).mapToModelList()
}

private fun Flow<ShopEntity?>.mapToModel(): Flow<Shop?> =
    this.map { entity -> entity?.toModel() }

private fun Flow<List<ShopEntity>>.mapToModelList(): Flow<List<Shop>> = this.map { entities ->
    entities.map { it.toModel() }
}

fun ShopId?.toFirestoreId(): FirestoreEntityId? = this?.toString()
fun ShopId.toFirestoreId(): FirestoreEntityId = this.toString()