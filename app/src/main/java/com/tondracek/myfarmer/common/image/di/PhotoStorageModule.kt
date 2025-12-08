package com.tondracek.myfarmer.common.image.di

import com.tondracek.myfarmer.common.image.data.PhotoStorage
import com.tondracek.myfarmer.common.image.data.PhotoStorageImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PhotoStorageModule {

    @Binds
    @Singleton
    abstract fun bindPhotoStorage(impl: PhotoStorageImpl): PhotoStorage
}
