package com.tondracek.myfarmer.shopcategory.data

import com.tondracek.myfarmer.core.firestore.FirestoreCollectionNames
import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.core.repository.firestore.FirestoreCollectionName
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntity
import com.tondracek.myfarmer.shopcategory.domain.model.CategoryPopularity
import javax.inject.Inject
import javax.inject.Singleton

@FirestoreCollectionName(FirestoreCollectionNames.CATEGORY_POPULARITY)
data class CategoryPopularityEntity(
    override var id: String = "",
    var count: Int = 0
) : FirestoreEntity

@Singleton
class CategoryPopularityMapper @Inject constructor() :
    EntityMapper<CategoryPopularity, CategoryPopularityEntity> {

    override fun toEntity(model: CategoryPopularity) = CategoryPopularityEntity(
        id = model.name,
        count = model.count
    )

    override fun toModel(entity: CategoryPopularityEntity) = CategoryPopularity(
        name = entity.id,
        count = entity.count
    )
}
