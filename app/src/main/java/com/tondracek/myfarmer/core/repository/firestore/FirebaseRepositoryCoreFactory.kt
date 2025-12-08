package com.tondracek.myfarmer.core.repository.firestore

import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.core.repository.RepositoryCoreFactory
import com.tondracek.myfarmer.core.repository.firestore.firestoreclient.FirestoreClientImpl

class FirebaseRepositoryCoreFactory<Entity : FirestoreEntity>() : RepositoryCoreFactory<Entity> {

    override fun <Model> create(
        mapper: EntityMapper<Model, Entity>,
        entityClass: Class<Entity>
    ): FirestoreRepositoryCore<Model, Entity> = FirestoreRepositoryCore(
        mapper = mapper,
        entityClass = entityClass,
        firestore = FirestoreClientImpl()
    )
}