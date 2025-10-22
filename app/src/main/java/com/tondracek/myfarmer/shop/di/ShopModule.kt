package com.tondracek.myfarmer.shop.di

import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.shop.data.ShopEntity
import com.tondracek.myfarmer.shop.data.ShopFBRepository
import com.tondracek.myfarmer.shop.data.ShopMapper
import com.tondracek.myfarmer.shop.data.ShopRepository
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.systemuser.data.UserRepository
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
    fun provideShopMapper(
        userRepository: UserRepository
    ): EntityMapper<Shop, ShopEntity> = ShopMapper(userRepository)


    @Provides
    @Singleton
    fun provideShopRepository(
        mapper: EntityMapper<Shop, ShopEntity>
    ): ShopRepository = ShopFBRepository(mapper)
}