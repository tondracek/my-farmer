package com.tondracek.myfarmer.shop.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.firestore
import com.tondracek.myfarmer.core.data.FirestoreCollectionNames
import com.tondracek.myfarmer.core.firestore.helpers.FirestoreCrudHelper
import com.tondracek.myfarmer.core.firestore.helpers.functions.firestoreGetByField
import com.tondracek.myfarmer.core.firestore.helpers.functions.firestoreGetPaginatedById
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntityId
import com.tondracek.myfarmer.location.data.DistanceRings
import com.tondracek.myfarmer.location.data.GeoHashUtils
import com.tondracek.myfarmer.location.model.Location
import com.tondracek.myfarmer.location.usecase.measureMapDistance
import com.tondracek.myfarmer.shop.data.firestore.firestoreGetByGeohashPaged
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.repository.DistancePagingCursor
import com.tondracek.myfarmer.shop.domain.repository.ShopRepository
import com.tondracek.myfarmer.systemuser.data.toFirestoreId
import com.tondracek.myfarmer.systemuser.domain.model.UserId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
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

    override suspend fun getAllPaginated(limit: Int?, after: ShopId?): List<Shop> =
        firestoreGetPaginatedById(
            collection = collection,
            entityClass = ShopEntity::class,
            limit = limit,
            after = after?.toString(),
        ).map { it.toModel() }

    override fun getByOwnerId(ownerId: UserId) = firestoreGetByField(
        collection = collection,
        entityClass = ShopEntity::class,
        field = FieldPath.of(ShopEntity::ownerId.name),
        value = ownerId.toFirestoreId(),
    ).mapToModelList()

    override suspend fun getPagedByDistance(
        center: Location,
        pageSize: Int,
        cursor: DistancePagingCursor?
    ): Pair<List<Shop>, DistancePagingCursor?> {
        val ringIndex = cursor?.ringIndex ?: 0
        val afterGeohash = cursor?.afterGeohash

        val ring = DistanceRings.rings[ringIndex]
        val results = mutableListOf<ShopEntity>()

        val ranges = GeoHashUtils.ranges(
            center = center,
            radiusMeters = ring.maxRadiusMeters,
        )

        for (range in ranges) {
            val query = firestoreGetByGeohashPaged(
                collection = collection,
                range = range,
                limit = pageSize + 1,
                afterGeohash = afterGeohash,
            )

            results += query

            if (results.size > pageSize) break
        }

        val page = results.take(pageSize)
            .map { it.toModel() }

        val nextCursor = when {
            results.size > pageSize -> // More data available in the current ring
                DistancePagingCursor(ringIndex, page.last().location.geohash)

            else -> // Move to the next ring
                DistancePagingCursor(ringIndex + 1, afterGeohash)
        }

        return (page to nextCursor).also { (list, cursor) ->
            println("XXX Paged shops by distance: returned ${page.size} items, nextCursor=$cursor. (center=$center, pageSize=$pageSize, ringIndex=$ringIndex, afterGeohash=$afterGeohash)")
            list.forEach {
                println("  XXX  Shop: name=${it.name}, distance=${measureMapDistance(center, it.location)}")
            }
        }
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