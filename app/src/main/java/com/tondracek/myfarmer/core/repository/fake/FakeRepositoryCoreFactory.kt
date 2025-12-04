package com.tondracek.myfarmer.core.repository.fake

import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.core.repository.RepositoryCore
import com.tondracek.myfarmer.core.repository.RepositoryCoreFactory
import com.tondracek.myfarmer.core.repository.RepositoryEntity
import java.util.UUID

class FakeRepositoryCoreFactory<Entity : RepositoryEntity<*>>(
    private val getUUID: Entity.() -> UUID
) : RepositoryCoreFactory<Entity> {

    override fun <Model> create(
        mapper: EntityMapper<Model, Entity>,
        entityClass: Class<Entity>
    ): RepositoryCore<Model> = FakeRepositoryCore(
        mapper = mapper,
        getUUID = getUUID
    )
}