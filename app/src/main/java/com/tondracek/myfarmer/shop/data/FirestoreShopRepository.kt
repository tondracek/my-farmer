package com.tondracek.myfarmer.shop.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.firestore
import com.tondracek.myfarmer.core.data.FirestoreCollectionNames
import com.tondracek.myfarmer.core.firestore.helpers.FirestoreCrudHelper
import com.tondracek.myfarmer.core.firestore.helpers.functions.firestoreGetByField
import com.tondracek.myfarmer.core.firestore.helpers.functions.firestoreGetPaginated
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntityId
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.repository.ShopRepository
import com.tondracek.myfarmer.systemuser.data.toFirestoreId
import com.tondracek.myfarmer.systemuser.domain.model.UserId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreShopRepository @Inject constructor(
) : ShopRepository {

    private val firestore = Firebase.firestore
    private val collection = firestore.collection(FirestoreCollectionNames.SHOP)

    private val helper = FirestoreCrudHelper(
        collection = collection,
        entityClass = ShopEntity::class,
    )

    override fun getAll(limit: Int?, after: ShopId?): Flow<List<Shop>> =
        firestoreGetPaginated(
            collection = collection,
            entityClass = ShopEntity::class,
            limit = limit,
            after = after?.toString(),
        ).mapToModelList()

    override fun getByOwnerId(ownerId: UserId) = firestoreGetByField(
        collection = collection,
        entityClass = ShopEntity::class,
        field = FieldPath.of(ShopEntity::ownerId.name),
        value = ownerId.toFirestoreId(),
    ).mapToModelList()

    override fun getPagedByDistance(): Flow<List<Shop>> {
        TODO("Not yet implemented")
    }

    override suspend fun create(item: Shop): ShopId =
        helper.create(item.toEntity()).toShopId()

    override suspend fun update(item: Shop) =
        helper.update(item.toEntity())

    override suspend fun delete(id: ShopId) =
        helper.delete(id.toFirestoreId())

    override fun getById(id: ShopId): Flow<Shop?> =
        helper.getById(id.toFirestoreId()).mapToModel()

    override fun getAll(): Flow<List<Shop>> =
        helper.getAll().mapToModelList()

    private fun Flow<ShopEntity?>.mapToModel(): Flow<Shop?> =
        this.map { entity -> entity?.toModel() }

    private fun Flow<List<ShopEntity>>.mapToModelList(): Flow<List<Shop>> = this.map { entities ->
        entities.map { it.toModel() }
    }
}

fun ShopId?.toFirestoreId(): FirestoreEntityId? = this?.toString()
fun ShopId.toFirestoreId(): FirestoreEntityId = this.toString()

fun FirestoreEntityId.toShopId(): ShopId = ShopId.fromString(this)