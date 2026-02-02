package com.tondracek.myfarmer.shopcategory.domain.repository

import com.tondracek.myfarmer.core.domain.domainresult.DomainResult
import com.tondracek.myfarmer.shopcategory.domain.model.CategoryPopularity
import kotlinx.coroutines.flow.Flow

interface CategoryPopularityRepository {
    fun getMostPopularCategories(limit: Int): Flow<DomainResult<List<CategoryPopularity>>>
}