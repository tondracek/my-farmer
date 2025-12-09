package com.tondracek.myfarmer.core.repository

import com.tondracek.myfarmer.core.repository.request.RepositoryRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

abstract class BaseRepository<Model, ModelId, Entity : RepositoryEntity<EntityId>, EntityId>(
    private val core: RepositoryCore<Entity, EntityId>,
    private val mapper: EntityMapper<Model, ModelId, Entity, EntityId>
) : Repository<Model, ModelId> {

    override suspend fun create(item: Model): ModelId {
        val entity = mapper.toEntity(item)
        val id = core.create(entity)
        return mapper.toModelId(id)
    }

    override suspend fun update(item: Model) {
        val model = mapper.toEntity(item)
        core.update(model)
    }

    override suspend fun delete(id: ModelId) {
        val entityId = mapper.toEntityId(id)
        core.delete(entityId)
    }

    override fun getById(id: ModelId): Flow<Model?> {
        val entityId = mapper.toEntityId(id)
        return core.getById(entityId)
            .map { entity ->
                entity?.let(mapper::toModel)
            }
    }

    override fun get(request: RepositoryRequest): Flow<List<Model>> = core.get(request)
        .map { entities -> entities.map(mapper::toModel) }

    override fun getAll(): Flow<List<Model>> = core.getAll().map { entities ->
        entities.map(mapper::toModel)
    }
}