package com.tondracek.myfarmer.core.repository.firestore

import com.tondracek.myfarmer.core.repository.RepositoryCore

object FirestoreRepositoryCoreFactory {

    inline fun <reified Entity : FirestoreEntity> create(): RepositoryCore<Entity, FirestoreEntityId> {
        val entityClass = Entity::class.java

        return FirestoreRepositoryCore(entityClass = entityClass)
    }
}