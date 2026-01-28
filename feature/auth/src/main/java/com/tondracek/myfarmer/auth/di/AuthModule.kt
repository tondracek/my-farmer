package com.tondracek.myfarmer.auth.di

import com.google.firebase.auth.FirebaseAuth
import com.tondracek.myfarmer.auth.data.FirebaseAuthRepository
import com.tondracek.myfarmer.auth.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AuthModule {

    @Provides
    @Singleton
    fun provideFirebaseInstance(): FirebaseAuth =
        FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository =
        FirebaseAuthRepository(firebaseAuth = firebaseAuth)
}
