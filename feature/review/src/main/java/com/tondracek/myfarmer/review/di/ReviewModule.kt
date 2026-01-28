package com.tondracek.myfarmer.review.di

import com.tondracek.myfarmer.review.data.FirestoreReviewRepository
import com.tondracek.myfarmer.review.domain.repository.ReviewRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ReviewModule {

    @Binds
    abstract fun bindReviewRepository(
        firestoreReviewRepository: FirestoreReviewRepository
    ): ReviewRepository
}