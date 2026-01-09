package com.tondracek.myfarmer.core.repository

import com.tondracek.myfarmer.core.repository.request.RepositoryRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

abstract class BaseRepository<Model, ModelId, Entity : RepositoryEntity<EntityId>, EntityId>(
    private val core: RepositoryCore<Entity, EntityId>,
    private val entityMapper: EntityMapper<Model, Entity>,
    private val idMapper: IdMapper<ModelId, EntityId>,
) : Repository<Model, ModelId> {

    override suspend fun create(item: Model): ModelId {
        val entity = entityMapper.toEntity(item)
        val id = core.create(entity)
        return idMapper.toModelId(id)
    }

    override suspend fun update(item: Model) {
        val model = entityMapper.toEntity(item)
        core.update(model)
    }

    override suspend fun delete(id: ModelId) {
        val entityId = idMapper.toEntityId(id)
        core.delete(entityId)
    }

    override fun getById(id: ModelId): Flow<Model?> {
        val entityId = idMapper.toEntityId(id)
        return core.getById(entityId)
            .map { entity ->
                entity?.let(entityMapper::toModel)
            }
    }

    protected fun get(request: RepositoryRequest): Flow<List<Model>> = core.get(request)
        .map { entities -> entities.map(entityMapper::toModel) }

    override fun getAll(): Flow<List<Model>> = core.getAll().map { entities ->
        entities.map(entityMapper::toModel)
    }
}