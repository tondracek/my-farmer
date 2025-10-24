package com.tondracek.myfarmer.review.data

import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ReviewEntity(
    var id: String = "",
    var userId: String = "",
    var shopId: String = "",
    var rating: Int = 0,
    var comment: String? = null,
)

fun ReviewEntity.toModel(user: SystemUser) = Review(
    id = UUID.fromString(id),
    user = user,
    rating = rating,
    comment = comment,
)

fun Review.toEntity() = ReviewEntity(
    id = id.toString(),
    userId = user.id.toString(),
    rating = rating,
    comment = comment,
)