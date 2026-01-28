package com.tondracek.myfarmer.shopcategory.data

import com.tondracek.myfarmer.core.data.firestore.FirestoreEntity
import com.tondracek.myfarmer.shopcategory.domain.model.CategoryPopularity

data class CategoryPopularityEntity(
    override var id: String = "",
    var count: Int = 0
) : FirestoreEntity

fun CategoryPopularityEntity.toModel() = CategoryPopularity(
    name = id,
    count = count
)
