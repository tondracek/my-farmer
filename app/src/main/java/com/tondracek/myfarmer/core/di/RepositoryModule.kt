package com.tondracek.myfarmer.core.di

import com.tondracek.myfarmer.core.repository.RepositoryCore
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntityId
import com.tondracek.myfarmer.core.repository.firestore.FirestoreRepositoryCoreFactory
import com.tondracek.myfarmer.shop.data.ShopEntity
import com.tondracek.myfarmer.shopcategory.data.CategoryPopularityEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    private val factory = FirestoreRepositoryCoreFactory

    @Provides
    @Singleton
    fun provideShopRepositoryCore(): RepositoryCore<ShopEntity, FirestoreEntityId> =
        factory.create<ShopEntity>()

    @Provides
    @Singleton
    fun provideCategoryPopularityRepositoryCore(): RepositoryCore<CategoryPopularityEntity, FirestoreEntityId> =
        factory.create<CategoryPopularityEntity>()
}