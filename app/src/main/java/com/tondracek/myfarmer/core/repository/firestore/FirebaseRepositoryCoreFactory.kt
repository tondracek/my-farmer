package com.tondracek.myfarmer.core.repository.firestore

import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.core.repository.RepositoryCore
import com.tondracek.myfarmer.core.repository.RepositoryCoreFactory

class FirebaseRepositoryCoreFactory<Entity : FirestoreEntity>() : RepositoryCoreFactory<Entity> {

    override fun <Model> create(
        mapper: EntityMapper<Model, Entity>,
        entityClass: Class<Entity>
    ): RepositoryCore<Model> = FirestoreRepositoryCore(mapper, entityClass)
}