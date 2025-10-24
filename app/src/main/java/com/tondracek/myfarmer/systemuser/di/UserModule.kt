package com.tondracek.myfarmer.systemuser.di

import com.tondracek.myfarmer.core.repository.Repository
import com.tondracek.myfarmer.systemuser.data.UserFBRepository
import com.tondracek.myfarmer.systemuser.data.UserRepository
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UserModule {

    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository = UserFBRepository()

    @Provides
    @Singleton
    fun provideUserRepository0(): Repository<SystemUser> = UserFBRepository()
}