package com.tondracek.myfarmer.systemuser.di

import com.tondracek.myfarmer.systemuser.data.FirestoreUserRepository
import com.tondracek.myfarmer.systemuser.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UserModule {

    @Binds
    abstract fun bindUserRepository(
        firestoreUserRepository: FirestoreUserRepository
    ): UserRepository
}