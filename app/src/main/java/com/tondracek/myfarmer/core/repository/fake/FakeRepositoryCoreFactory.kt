package com.tondracek.myfarmer.core.repository.fake

import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.core.repository.RepositoryCoreFactory
import com.tondracek.myfarmer.core.repository.RepositoryEntity
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntity
import java.util.UUID

open class FakeRepositoryCoreFactory<Entity : RepositoryEntity<*>>(
    private val getUUID: Entity.() -> UUID,
) : RepositoryCoreFactory<Entity> {

    override fun <Model> create(
        mapper: EntityMapper<Model, Entity>,
        entityClass: Class<Entity>,
    ): FakeRepositoryCore<Model, Entity> = FakeRepositoryCore(
        mapper = mapper,
        getUUID = getUUID
    )
}

class FakeRepositoryCoreFactoryFirestoreEntity<Entity : FirestoreEntity> :
    FakeRepositoryCoreFactory<Entity>({ UUID.fromString(this.id) })
