package com.tondracek.myfarmer.review.data

import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.snapshots
import com.tondracek.myfarmer.core.data.firestore.FirestoreCollectionNames
import com.tondracek.myfarmer.core.data.firestore.domainresult.domainResultOf
import com.tondracek.myfarmer.core.data.firestore.domainresult.toDomainResult
import com.tondracek.myfarmer.core.data.firestore.domainresult.toDomainResultNonNull
import com.tondracek.myfarmer.core.data.firestore.helpers.applyIfNotNull
import com.tondracek.myfarmer.core.data.firestore.helpers.getEntities
import com.tondracek.myfarmer.core.data.firestore.helpers.getEntitiesFlow
import com.tondracek.myfarmer.core.data.firestore.helpers.mapToDomain
import com.tondracek.myfarmer.core.domain.domainerror.ReviewError
import com.tondracek.myfarmer.core.domain.repository.firestore.FirestoreEntityId
import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.review.domain.model.ReviewId
import com.tondracek.myfarmer.review.domain.repository.ReviewPageCursor
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

class FirestoreReviewRepository @Inject constructor() : ReviewRepository {

    private val firestore = Firebase.firestore

    private fun reviewsCollection(shopId: ShopId) =
        firestore
            .collection(FirestoreCollectionNames.SHOP)
            .document(shopId.toFirestoreId())
            .collection(FirestoreCollectionNames.REVIEW)

    private fun reviewsCollection(shopId: ShopId, userId: UserId) =
        reviewsCollection(shopId)
            .document(userId.toFirestoreId())

    private fun allReviewsCollection() =
        firestore.collectionGroup(FirestoreCollectionNames.REVIEW)

    override fun getShopReviews(
        shopId: ShopId,
        limit: Int?,
    ): Flow<DomainResult<List<Review>>> = reviewsCollection(shopId)
        .orderBy(ReviewEntity::createdAt.name, Query.Direction.DESCENDING)
        .applyIfNotNull(limit) { this.limit(it.toLong()) }
        .getEntitiesFlow(ReviewEntity::class)
        .mapToModelList()
        .toDomainResult(ReviewError.FetchingFailed)

    override suspend fun getShopReviewsPaged(
        shopId: ShopId,
        limit: Int,
        after: ReviewPageCursor?,
    ): DomainResult<Pair<List<Review>, ReviewPageCursor?>> =
        domainResultOf(ReviewError.FetchingFailed) {
            val afterEntity = after?.let { Timestamp(it.createdAt) to it.id.toFirestoreId() }

            val reviewEntities = reviewsCollection(shopId)
                .orderBy(ReviewEntity::createdAt.name, Query.Direction.DESCENDING)
                .orderBy(ReviewEntity::id.name, Query.Direction.DESCENDING)
                .applyIfNotNull(afterEntity) { this.startAfter(it.first, it.second) }
                .limit(limit.toLong())
                .getEntities(ReviewEntity::class)

            val nextCursor = when {
                reviewEntities.size < limit -> null
                else -> {
                    val last = reviewEntities.last()
                    val createdAt = last.createdAt?.toInstant()
                    val id = last.id.toReviewId()

                    if (createdAt != null)
                        ReviewPageCursor(
                            createdAt = createdAt,
                            id = id
                        )
                    else null
                }
            }

            val reviews = reviewEntities.map { it.toModel() }
            reviews to nextCursor
        }

    override fun getUserReviewOnShop(
        shopId: ShopId,
        userId: UserId
    ): Flow<DomainResult<Review>> =
        reviewsCollection(shopId, userId)
            .snapshots()
            .mapToDomain(ReviewEntity::class) { it.toModel() }
            .toDomainResultNonNull(ReviewError.NotFound, ReviewError.FetchingFailed)

    override suspend fun create(item: Review): DomainResult<ReviewId> = domainResultOf(
        ReviewError.CreationFailed,
        ReviewAlreadyExistsException::class to ReviewError.AlreadyExists
    ) {
        firestore.runTransaction { tx ->
            val docRef = reviewsCollection(item.shopId, item.userId)

            if (tx.get(docRef).exists())
                throw ReviewAlreadyExistsException()

            tx.set(docRef, item.toEntity())

            item.id
        }.await()
    }

    override suspend fun update(item: Review): DomainResult<Unit> =
        domainResultOf(ReviewError.UpdateFailed) {
            reviewsCollection(item.shopId, item.userId)
                .set(item.toEntity())
                .await()
        }

    override suspend fun delete(id: ReviewId): DomainResult<Unit> =
        domainResultOf(ReviewError.DeletionFailed) {
            val entity = allReviewsCollection()
                .whereEqualTo(ReviewEntity::id.name, id.toFirestoreId())
                .getEntities(ReviewEntity::class)
                .firstOrNull()
                ?: return DomainResult.Failure(ReviewError.NotFound)

            reviewsCollection(
                ShopId.fromString(entity.shopId),
                UserId.fromString(entity.userId)
            )
                .delete()
                .await()
        }

    override fun getById(id: ReviewId): Flow<DomainResult<Review>> =
        allReviewsCollection()
            .whereEqualTo(ReviewEntity::id.name, id.toFirestoreId())
            .getEntitiesFlow(ReviewEntity::class)
            .mapToModelList()
            .map { it.firstOrNull() }
            .toDomainResultNonNull(ReviewError.NotFound, ReviewError.FetchingFailed)


    override fun getAll(): Flow<DomainResult<List<Review>>> =
        allReviewsCollection()
            .getEntitiesFlow(ReviewEntity::class)
            .mapToModelList()
            .toDomainResult(ReviewError.FetchingFailed)
}

private fun Flow<List<ReviewEntity>>.mapToModelList(): Flow<List<Review>> =
    this.map { userEntities ->
        userEntities.map { reviewEntity -> reviewEntity.toModel() }
    }

fun ReviewId?.toFirestoreId(): FirestoreEntityId? = this?.toString()
fun ReviewId.toFirestoreId(): FirestoreEntityId = this.toString()

private fun FirestoreEntityId.toReviewId() = ReviewId.fromString(this)