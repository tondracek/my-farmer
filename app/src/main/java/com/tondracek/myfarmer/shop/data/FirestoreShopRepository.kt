package com.tondracek.myfarmer.shop.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.firestore
import com.tondracek.myfarmer.core.data.firestore.FirestoreCollectionNames
import com.tondracek.myfarmer.core.data.firestore.helpers.FirestoreCrudHelper
import com.tondracek.myfarmer.core.data.firestore.helpers.functions.firestoreGetByField
import com.tondracek.myfarmer.core.data.firestore.helpers.functions.firestoreGetPaginatedById
import com.tondracek.myfarmer.core.domain.domainerror.ShopError
import com.tondracek.myfarmer.core.domain.repository.firestore.FirestoreEntityId
import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult
import com.tondracek.myfarmer.core.domain.usecaseresult.toUCResult
import com.tondracek.myfarmer.core.domain.usecaseresult.toUCResultNonNull
import com.tondracek.myfarmer.location.data.GeoHashUtils
import com.tondracek.myfarmer.location.model.DistanceRing
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
import javax.inject.Inject

class FirestoreShopRepository @Inject constructor(
) : ShopRepository {

    private val firestore = Firebase.firestore
    private val collection = firestore.collection(FirestoreCollectionNames.SHOP)

    private val helper = FirestoreCrudHelper(
        collection = collection,
        entityClass = ShopEntity::class,
    )

    override suspend fun getAllPaginated(limit: Int?, after: ShopId?): UCResult<List<Shop>> =
        UCResult.of(ShopError.FetchingFailed) {
            firestoreGetPaginatedById(
                collection = collection,
                entityClass = ShopEntity::class,
                limit = limit,
                after = after?.toString(),
            ).map { it.toModel() }
        }

    override fun getByOwnerId(ownerId: UserId): Flow<UCResult<List<Shop>>> = firestoreGetByField(
        collection = collection,
        entityClass = ShopEntity::class,
        field = FieldPath.of(ShopEntity::ownerId.name),
        value = ownerId.toFirestoreId(),
    ).mapToModelList()
        .toUCResult(ShopError.FetchingFailed)

    override suspend fun getPagedByDistance(
        center: Location,
        pageSize: Int,
        cursor: DistancePagingCursor?,
        rings: List<DistanceRing>
    ): UCResult<Pair<List<Shop>, DistancePagingCursor?>> = UCResult.of(ShopError.FetchingFailed) {
        val ringIndex = cursor?.ringIndex ?: 0
        val afterGeohash = cursor?.afterGeohash

        val ring = rings.getOrNull(ringIndex)
        if (ring == null)
            return UCResult.Success(emptyList<Shop>() to null) // No more rings available

        val ranges = GeoHashUtils.ranges(
            center = center,
            ring = ring
        )

        val results = mutableListOf<ShopEntity>()
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

        val nextCursor = when {
            results.size > pageSize -> // More data available in the current ring
                DistancePagingCursor(ringIndex, page.last().location.geohash)

            else -> // Move to the next ring
                DistancePagingCursor(ringIndex + 1, afterGeohash)
        }

        val domainPage = page.map { it.toModel() }
            .sortedBy { measureMapDistance(it.location, center) }
        return UCResult.Success(domainPage to nextCursor)
    }

    override suspend fun create(item: Shop): UCResult<ShopId> =
        UCResult.of(ShopError.CreationFailed) {
            helper.create(item.toEntity()).toShopId()
        }

    override suspend fun update(item: Shop) = UCResult.of(ShopError.UpdateFailed) {
        helper.update(item.toEntity())
    }

    override suspend fun delete(id: ShopId) = UCResult.of(ShopError.DeletionFailed) {
        helper.delete(id.toFirestoreId())
    }

    override fun getById(id: ShopId): Flow<UCResult<Shop>> =
        helper.getById(id.toFirestoreId()).mapToModel()
            .toUCResultNonNull(ShopError.NotFound, ShopError.Unknown)

    override fun getAll(): Flow<UCResult<List<Shop>>> =
        helper.getAll()
            .mapToModelList()
            .toUCResult(ShopError.FetchingFailed)

    private fun Flow<ShopEntity?>.mapToModel(): Flow<Shop?> =
        this.map { entity -> entity?.toModel() }

    private fun Flow<List<ShopEntity>>.mapToModelList(): Flow<List<Shop>> = this.map { entities ->
        entities.map { it.toModel() }
    }
}

fun ShopId?.toFirestoreId(): FirestoreEntityId? = this?.toString()
fun ShopId.toFirestoreId(): FirestoreEntityId = this.toString()

fun FirestoreEntityId.toShopId(): ShopId = ShopId.fromString(this)