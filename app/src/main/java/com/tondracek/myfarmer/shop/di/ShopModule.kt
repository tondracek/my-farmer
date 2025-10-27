package com.tondracek.myfarmer.shop.di

import com.tondracek.myfarmer.core.di.RepositoryCoreFactory
import com.tondracek.myfarmer.core.repository.Repository
import com.tondracek.myfarmer.core.repository.RepositoryCore
import com.tondracek.myfarmer.shop.data.ShopEntity
import com.tondracek.myfarmer.shop.data.ShopMapper
import com.tondracek.myfarmer.shop.data.ShopRepository
import com.tondracek.myfarmer.shop.domain.model.Shop
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ShopModule {

    @Provides
    @Singleton
    fun provideShopRepository(repository: ShopRepository): Repository<Shop> = repository

    @Provides
    @Singleton
    fun provideShopRepositoryCore(
        factory: RepositoryCoreFactory,
        mapper: ShopMapper
    ): RepositoryCore<Shop> = factory.create(mapper, ShopEntity::class.java)
}
