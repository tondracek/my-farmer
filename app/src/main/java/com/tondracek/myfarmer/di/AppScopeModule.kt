package com.tondracek.myfarmer.di

import com.tondracek.myfarmer.core.domain.coroutine.AppCoroutineScope
import com.tondracek.myfarmer.coroutine.AppCoroutineScopeImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppScopeModule {

    @Provides
    @Singleton
    fun provideAppScope(): AppCoroutineScope = AppCoroutineScopeImpl()
}