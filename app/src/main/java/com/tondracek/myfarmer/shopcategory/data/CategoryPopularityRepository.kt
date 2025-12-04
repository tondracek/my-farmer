package com.tondracek.myfarmer.shopcategory.data

import com.tondracek.myfarmer.core.repository.BaseRepository
import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.core.repository.RepositoryCore
import com.tondracek.myfarmer.shopcategory.domain.model.CategoryPopularity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryPopularityRepository @Inject constructor(
    core: RepositoryCore<CategoryPopularity>,
) : BaseRepository<CategoryPopularity>(core)

@Singleton
class CategoryPopularityMapper @Inject constructor() :
    EntityMapper<CategoryPopularity, CategoryPopularityEntity> {
    override fun toEntity(model: CategoryPopularity): CategoryPopularityEntity =
        model.toEntity()

    override fun toModel(entity: CategoryPopularityEntity): CategoryPopularity =
        entity.toModel()
}