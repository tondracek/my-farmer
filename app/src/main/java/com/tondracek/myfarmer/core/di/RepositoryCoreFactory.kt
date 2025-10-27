package com.tondracek.myfarmer.core.di

import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntity
import com.tondracek.myfarmer.core.repository.firestore.FirestoreRepositoryCore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryCoreFactory @Inject constructor() {

    fun <Model, Entity : FirestoreEntity> create(
        mapper: EntityMapper<Model, Entity>,
        entityClass: Class<Entity>
    ): FirestoreRepositoryCore<Model, Entity> = FirestoreRepositoryCore(mapper, entityClass)
}