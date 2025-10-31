package com.tondracek.myfarmer.review.data

import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.model.Review
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

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
