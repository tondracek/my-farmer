package com.tondracek.myfarmer.core.di

import com.tondracek.myfarmer.core.repository.RepositoryCore
import com.tondracek.myfarmer.core.repository.firestore.FirebaseRepositoryCoreFactory
import com.tondracek.myfarmer.review.data.ReviewEntity
import com.tondracek.myfarmer.review.data.ReviewMapper
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.shop.data.ShopEntity
import com.tondracek.myfarmer.shop.data.ShopMapper
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shopcategory.data.CategoryPopularityEntity
import com.tondracek.myfarmer.shopcategory.data.CategoryPopularityMapper
import com.tondracek.myfarmer.shopcategory.domain.model.CategoryPopularity
import com.tondracek.myfarmer.systemuser.data.UserEntity
import com.tondracek.myfarmer.systemuser.data.UserEntityMapper
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepositoryCore(): RepositoryCore<SystemUser> =
        FirebaseRepositoryCoreFactory<UserEntity>()
            .create(UserEntityMapper(), UserEntity::class.java)

    @Provides
    @Singleton
    fun provideShopRepositoryCore(): RepositoryCore<Shop> =
        FirebaseRepositoryCoreFactory<ShopEntity>()
            .create(ShopMapper(), ShopEntity::class.java)

    @Provides
    @Singleton
    fun provideReviewRepositoryCore(): RepositoryCore<Review> =
        FirebaseRepositoryCoreFactory<ReviewEntity>()
            .create(ReviewMapper(), ReviewEntity::class.java)

    @Provides
    @Singleton
    fun provideCategoryPopularityRepositoryCore(): RepositoryCore<CategoryPopularity> =
        FirebaseRepositoryCoreFactory<CategoryPopularityEntity>()
            .create(CategoryPopularityMapper(), CategoryPopularityEntity::class.java)
}