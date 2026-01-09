package com.tondracek.myfarmer.review.data

import com.tondracek.myfarmer.core.firestore.FirestoreCollectionNames
import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.core.repository.firestore.FirestoreCollectionName
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntity
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.review.domain.model.ReviewId
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.systemuser.domain.model.UserId
import kotlinx.serialization.Serializable
import javax.inject.Inject
import javax.inject.Singleton

@FirestoreCollectionName(FirestoreCollectionNames.REVIEW)
@Serializable
data class ReviewEntity(
    override var id: String = "",
    var userId: String = "",
    var shopId: String = "",
    var rating: Int = 0,
    var comment: String? = null,
) : FirestoreEntity

@Singleton
class ReviewMapper @Inject constructor() : EntityMapper<Review, ReviewEntity> {

    override fun toModel(entity: ReviewEntity): Review = Review(
        id = ReviewId.fromString(entity.id),
        shopId = ShopId.fromString(entity.shopId),
        userId = UserId.fromString(entity.userId),
        rating = Rating(entity.rating),
        comment = entity.comment,
    )

    override fun toEntity(model: Review): ReviewEntity = ReviewEntity(
        id = model.id.toString(),
        shopId = model.shopId.toString(),
        userId = model.userId.toString(),
        rating = model.rating.stars,
        comment = model.comment,
    )
}
