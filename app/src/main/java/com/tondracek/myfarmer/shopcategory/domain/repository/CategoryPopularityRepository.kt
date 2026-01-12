package com.tondracek.myfarmer.shopcategory.domain.repository

import com.tondracek.myfarmer.shopcategory.domain.model.CategoryPopularity
import kotlinx.coroutines.flow.Flow

interface CategoryPopularityRepository {
    fun getMostPopularCategories(limit: Int): Flow<List<CategoryPopularity>>
}