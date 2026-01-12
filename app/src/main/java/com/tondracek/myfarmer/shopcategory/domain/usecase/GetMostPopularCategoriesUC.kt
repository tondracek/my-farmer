package com.tondracek.myfarmer.shopcategory.domain.usecase

import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult
import com.tondracek.myfarmer.core.domain.usecaseresult.toUCResult
import com.tondracek.myfarmer.shopcategory.domain.model.CategoryPopularity
import com.tondracek.myfarmer.shopcategory.domain.repository.CategoryPopularityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMostPopularCategoriesUC @Inject constructor(
    private val repository: CategoryPopularityRepository
) {
    operator fun invoke(): Flow<UCResult<List<CategoryPopularity>>> =
        repository.getMostPopularCategories(limit = 500)
            .toUCResult("Couldn't fetch most popular categories")
}