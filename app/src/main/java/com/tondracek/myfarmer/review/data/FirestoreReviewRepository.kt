package com.tondracek.myfarmer.review.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.snapshots
import com.tondracek.myfarmer.core.data.firestore.FirestoreCollectionNames
import com.tondracek.myfarmer.core.data.firestore.helpers.applyIfNotNull
import com.tondracek.myfarmer.core.data.firestore.helpers.getEntities
import com.tondracek.myfarmer.core.data.firestore.helpers.getEntitiesFlow
import com.tondracek.myfarmer.core.data.firestore.helpers.mapToDomain
import com.tondracek.myfarmer.core.domain.domainerror.ReviewError
import com.tondracek.myfarmer.core.domain.repository.firestore.FirestoreEntityId
import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult
import com.tondracek.myfarmer.core.domain.usecaseresult.toUCResult
import com.tondracek.myfarmer.core.domain.usecaseresult.toUCResultNonNull
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.review.domain.model.ReviewId
import com.tondracek.myfarmer.review.domain.repository.ReviewRepository
import com.tondracek.myfarmer.shop.data.toFirestoreId
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.systemuser.data.toFirestoreId
import com.tondracek.myfarmer.systemuser.domain.model.UserId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private class ReviewAlreadyExistsException : Exception()

private class ReviewNotFoundException : Exception()

class FirestoreReviewRepository @Inject constructor() : ReviewRepository {

    private val firestore = Firebase.firestore

    private fun reviewsCollection(shopId: ShopId) =
        firestore
            .collection(FirestoreCollectionNames.SHOP)
            .document(shopId.toFirestoreId())
            .collection("reviews")

    private fun reviewsCollection(shopId: ShopId, userId: UserId) =
        reviewsCollection(shopId)
            .document(userId.toFirestoreId())

    private fun allReviewsCollection() =
        firestore.collectionGroup("reviews")

    override fun getShopReviews(
        shopId: ShopId,
        limit: Int?,
    ): Flow<UCResult<List<Review>>> = reviewsCollection(shopId)
        .applyIfNotNull(limit) { this.limit(it.toLong()) }
        .getEntitiesFlow(ReviewEntity::class)
        .mapToModelList()
        .toUCResult(ReviewError.FetchingFailed)

    override suspend fun getShopReviewsPaged(
        shopId: ShopId,
        limit: Int,
        after: ReviewId?
    ): UCResult<List<Review>> = UCResult.of(ReviewError.FetchingFailed) {
        reviewsCollection(shopId)
            .applyIfNotNull(after) { id -> this.startAfter(id.toFirestoreId()) }
            .limit(limit.toLong())
            .getEntities(ReviewEntity::class)
            .map { it.toModel() }
    }

    override fun getUserReviewOnShop(
        shopId: ShopId,
        userId: UserId
    ): Flow<UCResult<Review>> =
        reviewsCollection(shopId, userId)
            .snapshots()
            .mapToDomain(ReviewEntity::class) { it.toModel() }
            .toUCResultNonNull(ReviewError.NotFound, ReviewError.FetchingFailed)

    override suspend fun create(item: Review): UCResult<ReviewId> = try {
        firestore.runTransaction { tx ->
            val docRef = reviewsCollection(item.shopId, item.userId)

            if (tx.get(docRef).exists())
                throw ReviewAlreadyExistsException()

            tx.set(docRef, item.toEntity())
        }.await()

        UCResult.Success(item.id)
    } catch (e: ReviewAlreadyExistsException) {
        UCResult.Failure(ReviewError.AlreadyExists, e)
    } catch (e: Exception) {
        UCResult.Failure(ReviewError.CreationFailed, e)
    }

    override suspend fun update(item: Review): UCResult<Unit> =
        UCResult.of(ReviewError.UpdateFailed) {
            reviewsCollection(item.shopId, item.userId)
                .set(item.toEntity())
                .await()
        }

    override suspend fun delete(id: ReviewId): UCResult<Unit> =
        UCResult.of(ReviewError.DeletionFailed) {
            val entity = allReviewsCollection()
                .whereEqualTo(ReviewEntity::id.name, id.toFirestoreId())
                .getEntities(ReviewEntity::class)
                .firstOrNull()
                ?: return UCResult.Failure(ReviewError.NotFound)


            reviewsCollection(
                ShopId.fromString(entity.shopId),
                UserId.fromString(entity.userId)
            )
                .delete()
                .await()
        }

    override fun getById(id: ReviewId): Flow<UCResult<Review>> =
        allReviewsCollection()
            .whereEqualTo(ReviewEntity::id.name, id.toFirestoreId())
            .getEntitiesFlow(ReviewEntity::class)
            .mapToModelList()
            .map { it.firstOrNull() }
            .toUCResultNonNull(ReviewError.NotFound, ReviewError.FetchingFailed)


    override fun getAll(): Flow<UCResult<List<Review>>> =
        allReviewsCollection()
            .getEntitiesFlow(ReviewEntity::class)
            .mapToModelList()
            .toUCResult(ReviewError.FetchingFailed)
}

private fun Flow<List<ReviewEntity>>.mapToModelList(): Flow<List<Review>> =
    this.map { userEntities ->
        userEntities.map { reviewEntity -> reviewEntity.toModel() }
    }

fun ReviewId?.toFirestoreId(): FirestoreEntityId? = this?.toString()
fun ReviewId.toFirestoreId(): FirestoreEntityId = this.toString()

private fun FirestoreEntityId.toReviewId() = ReviewId.fromString(this)