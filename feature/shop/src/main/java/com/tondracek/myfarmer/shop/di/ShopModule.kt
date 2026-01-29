package com.tondracek.myfarmer.shop.di

import com.tondracek.myfarmer.shop.data.firestore.FirestoreShopRepository
import com.tondracek.myfarmer.shop.domain.repository.ShopRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ShopModule {

    @Binds
    abstract fun bindShopRepository(
        firestoreShopRepository: FirestoreShopRepository
    ): ShopRepository
}