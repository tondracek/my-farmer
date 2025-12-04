package com.tondracek.myfarmer.core.repository

interface RepositoryCoreFactory<Entity : RepositoryEntity<*>> {
    fun <Model> create(
        mapper: EntityMapper<Model, Entity>,
        entityClass: Class<Entity>
    ): RepositoryCore<Model>
}
