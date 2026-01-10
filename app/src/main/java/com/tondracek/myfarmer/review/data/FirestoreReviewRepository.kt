package com.tondracek.myfarmer.review.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.snapshots
import com.tondracek.myfarmer.core.data.FirestoreCollectionNames
import com.tondracek.myfarmer.core.firestore.helpers.FirestoreCrudHelper
import com.tondracek.myfarmer.core.firestore.helpers.mapToEntities
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntityId
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.review.domain.model.ReviewId
import com.tondracek.myfarmer.review.domain.repository.ReviewRepository
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

    override fun getReviewsPreview(shopId: ShopId): Flow<List<Review>> =
        collection
            .whereEqualTo(ReviewEntity::shopId.name, shopId.toString())
            .limit(3)
            .snapshots()
            .mapToEntities(ReviewEntity::class)
            .mapToModelList()

    override fun getShopReviews(
        shopId: ShopId,
        limit: Int?,
        after: ReviewId?
    ): Flow<List<Review>> =
        collection
            .whereEqualTo(ReviewEntity::shopId.name, shopId.toString())
            .orderBy(FieldPath.documentId())
            .startAfterNullable(after)
            .limitNullable(limit)
            .snapshots()
            .mapToEntities(ReviewEntity::class)
            .mapToModelList()

    private fun Query.startAfterNullable(lastReviewId: ReviewId?): Query =
        lastReviewId?.let { id -> this.startAfter(id.toString()) } ?: this

    private fun Query.limitNullable(limit: Int?): Query =
        limit?.let { this.limit(it.toLong()) } ?: this


    override suspend fun create(item: Review): ReviewId =
        helper.create(item.toEntity()).toReviewId()

    override suspend fun update(item: Review) =
        helper.update(item.toEntity())

    override suspend fun delete(id: ReviewId) =
        helper.delete(id.toString())

    override fun getById(id: ReviewId): Flow<Review?> =
        helper.getById(id.toString()).mapToModel()

    override fun getAll(): Flow<List<Review>> =
        helper.getAll().mapToModelList()
}

private fun Flow<ReviewEntity?>.mapToModel(): Flow<Review?> =
    this.map { reviewEntity -> reviewEntity?.toModel() }

private fun Flow<List<ReviewEntity>>.mapToModelList(): Flow<List<Review>> =
    this.map { userEntities ->
        userEntities.map { reviewEntity -> reviewEntity.toModel() }
    }

private fun FirestoreEntityId.toReviewId() = ReviewId.fromString(this)