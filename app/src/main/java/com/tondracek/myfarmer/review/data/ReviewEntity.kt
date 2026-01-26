package com.tondracek.myfarmer.review.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import com.tondracek.myfarmer.core.domain.repository.firestore.FirestoreEntity
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.review.domain.model.ReviewId
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.systemuser.domain.model.UserId
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class ReviewEntity(
    override var id: String = "",
    var userId: String = "",
    var shopId: String = "",
    var rating: Int = 0,
    var comment: String? = null,
    @Contextual
    @ServerTimestamp
    var createdAt: Timestamp? = null
) : FirestoreEntity

fun ReviewEntity.toModel(): Review = Review(
    id = ReviewId.fromString(id),
    shopId = ShopId.fromString(shopId),
    userId = UserId.fromString(userId),
    rating = Rating(rating),
    comment = comment,
    createdAt = createdAt?.toInstant(),
)

fun Review.toEntity(): ReviewEntity = ReviewEntity(
    id = id.toString(),
    shopId = shopId.toString(),
    userId = userId.toString(),
    rating = rating.stars,
    comment = comment,
    // createdAt intentionally omitted
)
