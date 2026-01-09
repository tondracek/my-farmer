package com.tondracek.myfarmer.shopcategory.domain.model

typealias CategoryPopularityId = String

data class CategoryPopularity(
    val name: CategoryPopularityId,
    val count: Int
)