package com.tondracek.myfarmer.review.data

import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.core.repository.firestore.FirestoreCollectionName
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntity
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.model.Review
import kotlinx.serialization.Serializable
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@FirestoreCollectionName("review")
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
        id = UUID.fromString(entity.id),
        shopId = UUID.fromString(entity.shopId),
        userId = UUID.fromString(entity.userId),
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
