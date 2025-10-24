package com.tondracek.myfarmer.shop.di

import com.tondracek.myfarmer.core.repository.Repository
import com.tondracek.myfarmer.shop.data.ShopFBRepository
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
    fun provideShopRepository(): ShopRepository = ShopFBRepository()

    @Provides
    @Singleton
    fun provideShopRepository0(): Repository<Shop> = ShopFBRepository()
}