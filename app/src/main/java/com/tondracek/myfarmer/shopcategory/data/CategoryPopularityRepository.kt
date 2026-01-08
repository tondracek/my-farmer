package com.tondracek.myfarmer.shopcategory.data

import com.tondracek.myfarmer.core.repository.BaseRepository
import com.tondracek.myfarmer.core.repository.IdMapper
import com.tondracek.myfarmer.core.repository.RepositoryCore
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntityId
import com.tondracek.myfarmer.core.repository.request.DescendingSort
import com.tondracek.myfarmer.core.repository.request.repositoryRequest
import com.tondracek.myfarmer.shopcategory.domain.model.CategoryPopularity
import com.tondracek.myfarmer.shopcategory.domain.model.CategoryPopularityId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryPopularityRepository @Inject constructor(
    core: RepositoryCore<CategoryPopularityEntity, FirestoreEntityId>,
    mapper: CategoryPopularityMapper,
) : BaseRepository<CategoryPopularity, CategoryPopularityId, CategoryPopularityEntity, FirestoreEntityId>(
    core = core,
    entityMapper = mapper,
    idMapper = IdMapper.StringToString,
) {

    fun getMostPopularCategories(limit: Int) = get(
        repositoryRequest {
            addSort(DescendingSort(CategoryPopularityEntity::count))
            setLimit(limit)
        }
    )
}
