package com.tondracek.myfarmer.review.di

import com.tondracek.myfarmer.core.di.RepositoryCoreFactory
import com.tondracek.myfarmer.core.repository.Repository
import com.tondracek.myfarmer.core.repository.RepositoryCore
import com.tondracek.myfarmer.review.data.ReviewEntity
import com.tondracek.myfarmer.review.data.ReviewMapper
import com.tondracek.myfarmer.review.data.ReviewRepository
import com.tondracek.myfarmer.review.domain.model.Review
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ReviewModule {

    @Provides
    @Singleton
    fun provideReviewRepository(repository: ReviewRepository): Repository<Review> = repository

    @Provides
    @Singleton
    fun provideReviewRepositoryCore(
        factory: RepositoryCoreFactory,
        mapper: ReviewMapper
    ): RepositoryCore<Review> = factory.create(mapper, ReviewEntity::class.java)
}
