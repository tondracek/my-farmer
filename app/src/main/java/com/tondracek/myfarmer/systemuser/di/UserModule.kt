package com.tondracek.myfarmer.systemuser.di

import com.tondracek.myfarmer.core.di.RepositoryCoreFactory
import com.tondracek.myfarmer.core.repository.Repository
import com.tondracek.myfarmer.core.repository.RepositoryCore
import com.tondracek.myfarmer.systemuser.data.UserEntity
import com.tondracek.myfarmer.systemuser.data.UserEntityMapper
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
    fun provideUserRepository(repository: UserRepository): Repository<SystemUser> = repository

    @Provides
    @Singleton
    fun provideUserRepositoryCore(
        factory: RepositoryCoreFactory,
        mapper: UserEntityMapper
    ): RepositoryCore<SystemUser> = factory.create(mapper, UserEntity::class.java)
}
