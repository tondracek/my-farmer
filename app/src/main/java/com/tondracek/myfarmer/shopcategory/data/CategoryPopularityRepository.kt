package com.tondracek.myfarmer.shopcategory.data

import com.tondracek.myfarmer.core.di.RepositoryCoreFactory
import com.tondracek.myfarmer.core.repository.BaseRepository
import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.shopcategory.domain.model.CategoryPopularity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryPopularityRepository @Inject constructor(
    mapper: CategoryPopularityMapper,
    factory: RepositoryCoreFactory,
) : BaseRepository<CategoryPopularity>(
    factory.create(mapper, CategoryPopularityEntity::class.java)
)

@Singleton
class CategoryPopularityMapper @Inject constructor() :
    EntityMapper<CategoryPopularity, CategoryPopularityEntity> {
    override fun toEntity(model: CategoryPopularity): CategoryPopularityEntity =
        model.toEntity()

    override fun toModel(entity: CategoryPopularityEntity): CategoryPopularity =
        entity.toModel()
}