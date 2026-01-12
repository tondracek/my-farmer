package com.tondracek.myfarmer.review.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.firestore
import com.tondracek.myfarmer.core.data.firestore.FirestoreCollectionNames
import com.tondracek.myfarmer.core.data.firestore.helpers.FirestoreCrudHelper
import com.tondracek.myfarmer.core.data.firestore.helpers.functions.firestoreGetPaginatedFilteredByField
import com.tondracek.myfarmer.core.data.firestore.helpers.getEntitiesFlow
import com.tondracek.myfarmer.core.domain.repository.firestore.FirestoreEntityId
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.review.domain.model.ReviewId
import com.tondracek.myfarmer.review.domain.repository.ReviewRepository
import com.tondracek.myfarmer.shop.data.toFirestoreId
import com.tondracek.myfarmer.shop.domain.model.ShopId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FirestoreReviewRepository @Inject constructor() : ReviewRepository {

    private val firestore = Firebase.firestore
    private val collection = firestore.collection(FirestoreCollectionNames.REVIEW)

    private val helper = FirestoreCrudHelper(
        collection = collection,
        entityClass = ReviewEntity::class,
    )

    override fun getShopReviews(
        shopId: ShopId,
        limit: Int?,
    ): Flow<List<Review>> = collection
        .whereEqualTo(FieldPath.of(ReviewEntity::shopId.name), shopId.toFirestoreId())
        .let { query ->
            limit?.let { query.limit(it.toLong()) } ?: query
        }
        .getEntitiesFlow(ReviewEntity::class)
        .mapToModelList()

    override suspend fun getShopReviewsPaged(
        shopId: ShopId,
        limit: Int,
        after: ReviewId?
    ): List<Review> = firestoreGetPaginatedFilteredByField(
        collection = collection,
        entityClass = ReviewEntity::class,
        field = FieldPath.of(ReviewEntity::shopId.name),
        value = shopId.toFirestoreId(),
        limit = limit,
        after = after.toFirestoreId(),
    ).map { it.toModel() }

    override suspend fun create(item: Review): ReviewId =
        helper.create(item.toEntity()).toReviewId()

    override suspend fun update(item: Review) =
        helper.update(item.toEntity())

    override suspend fun delete(id: ReviewId) =
        helper.delete(id.toFirestoreId())

    override fun getById(id: ReviewId): Flow<Review?> =
        helper.getById(id.toFirestoreId()).mapToModel()

    override fun getAll(): Flow<List<Review>> =
        helper.getAll().mapToModelList()
}

private fun Flow<ReviewEntity?>.mapToModel(): Flow<Review?> =
    this.map { reviewEntity -> reviewEntity?.toModel() }

private fun Flow<List<ReviewEntity>>.mapToModelList(): Flow<List<Review>> =
    this.map { userEntities ->
        userEntities.map { reviewEntity -> reviewEntity.toModel() }
    }

fun ReviewId?.toFirestoreId(): FirestoreEntityId? = this?.toString()
fun ReviewId.toFirestoreId(): FirestoreEntityId = this.toString()

private fun FirestoreEntityId.toReviewId() = ReviewId.fromString(this)