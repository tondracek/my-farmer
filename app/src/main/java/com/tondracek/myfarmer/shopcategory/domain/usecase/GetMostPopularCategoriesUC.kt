package com.tondracek.myfarmer.shopcategory.domain.usecase

import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.core.usecaseresult.toUCResult
import com.tondracek.myfarmer.shopcategory.data.CategoryPopularityRepository
import com.tondracek.myfarmer.shopcategory.domain.model.CategoryPopularity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMostPopularCategoriesUC @Inject constructor(
    private val repository: CategoryPopularityRepository
) {
    operator fun invoke(): Flow<UCResult<List<CategoryPopularity>>> =
        repository.getMostPopularCategories(limit = 500)
            .toUCResult("Couldn't fetch most popular categories")
}