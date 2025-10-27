package com.tondracek.myfarmer.review.data

import com.tondracek.myfarmer.core.repository.firestore.FirestoreCollection
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntity
import kotlinx.serialization.Serializable

@FirestoreCollection("review")
@Serializable
data class ReviewEntity(
    override var id: String = "",
    var userId: String = "",
    var shopId: String = "",
    var rating: Int = 0,
    var comment: String? = null,
) : FirestoreEntity

