package com.tondracek.myfarmer.demo.di

import com.tondracek.myfarmer.demo.data.DemoFBRepository
import com.tondracek.myfarmer.demo.data.DemoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DemoModule {

    @Provides
    @Singleton
    fun provideDemoRepository(): DemoRepository = DemoFBRepository()
}