package com.tondracek.myfarmer.systemuser.data.di

import com.tondracek.myfarmer.systemuser.data.FirestoreUserReadAdapter
import com.tondracek.myfarmer.systemuser.domain.port.GetUserByIdPort
import com.tondracek.myfarmer.systemuser.domain.port.GetUsersByIdsPort
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UserDataModule {

    @Binds
    abstract fun bindGetUserByIdPort(impl: FirestoreUserReadAdapter): GetUserByIdPort

    @Binds
    abstract fun bindGetUsersByIdsPort(impl: FirestoreUserReadAdapter): GetUsersByIdsPort
}