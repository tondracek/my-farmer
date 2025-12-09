package com.tondracek.myfarmer.core.di

import com.tondracek.myfarmer.core.repository.RepositoryCore
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntityId
import com.tondracek.myfarmer.core.repository.firestore.FirestoreRepositoryCoreFactory
import com.tondracek.myfarmer.review.data.ReviewEntity
import com.tondracek.myfarmer.shop.data.ShopEntity
import com.tondracek.myfarmer.shopcategory.data.CategoryPopularityEntity
import com.tondracek.myfarmer.systemuser.data.UserEntity
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
    fun provideUserRepositoryCore(): RepositoryCore<UserEntity, FirestoreEntityId> =
        factory.create<UserEntity>()

    @Provides
    @Singleton
    fun provideShopRepositoryCore(): RepositoryCore<ShopEntity, FirestoreEntityId> =
        factory.create<ShopEntity>()

    @Provides
    @Singleton
    fun provideReviewRepositoryCore(): RepositoryCore<ReviewEntity, FirestoreEntityId> =
        factory.create<ReviewEntity>()

    @Provides
    @Singleton
    fun provideCategoryPopularityRepositoryCore(): RepositoryCore<CategoryPopularityEntity, FirestoreEntityId> =
        factory.create<CategoryPopularityEntity>()
}