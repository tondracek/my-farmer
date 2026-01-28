package com.tondracek.myfarmer.shopcategory.di

import com.tondracek.myfarmer.shopcategory.data.FirestoreCategoryPopularityRepository
import com.tondracek.myfarmer.shopcategory.domain.repository.CategoryPopularityRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ShopCategoryModule {

    @Binds
    abstract fun bindCategoryPopularityRepository(
        impl: FirestoreCategoryPopularityRepository
    ): CategoryPopularityRepository
}