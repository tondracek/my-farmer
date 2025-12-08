package com.tondracek.myfarmer.shopcategory.data

import com.tondracek.myfarmer.core.repository.firestore.FirestoreCollectionName
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntity
import com.tondracek.myfarmer.shopcategory.domain.model.CategoryPopularity

@FirestoreCollectionName("category_popularity")
data class CategoryPopularityEntity(
    override var id: String = "",
    var count: Int = 0
) : FirestoreEntity

fun CategoryPopularityEntity.toModel() = CategoryPopularity(
    name = id,
    count = count
)

fun CategoryPopularity.toEntity() = CategoryPopularityEntity(
    id = name,
    count = count
)