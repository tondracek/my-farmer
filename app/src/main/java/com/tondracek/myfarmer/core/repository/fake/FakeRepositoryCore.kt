package com.tondracek.myfarmer.core.repository.fake

import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.core.repository.RepositoryCore
import com.tondracek.myfarmer.core.repository.RepositoryEntity
import com.tondracek.myfarmer.core.repository.fake.FakeQueryBuilder.applyFilters
import com.tondracek.myfarmer.core.repository.fake.FakeQueryBuilder.applyLimit
import com.tondracek.myfarmer.core.repository.fake.FakeQueryBuilder.applyOffset
import com.tondracek.myfarmer.core.repository.fake.FakeQueryBuilder.applySorts
import com.tondracek.myfarmer.core.repository.request.RepositoryRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.util.UUID

class FakeRepositoryCore<Model, Entity : RepositoryEntity<*>>(
    private val mapper: EntityMapper<Model, Entity>,
    private val getUUID: Entity.() -> UUID,
) : RepositoryCore<Model> {

    val items: MutableMap<UUID, MutableStateFlow<Entity>> = mutableMapOf()

    override suspend fun create(item: Model): UUID {
        val entity = mapper.toEntity(item)
        val id = entity.getUUID()
        items[id] = MutableStateFlow(entity)
        return id
    }

    override suspend fun update(item: Model) {
        val entity = mapper.toEntity(item)
        val id = entity.getUUID()
        items[id]?.value = entity
    }

    override suspend fun delete(id: UUID) {
        items.remove(id)
    }

    override fun getById(id: UUID): Flow<Model?> {
        return items[id]?.map { mapper.toModel(it) } ?: flowOf(null)
    }

    override fun get(request: RepositoryRequest): Flow<List<Model>> {
        val flows: List<Flow<Any>> = items.values.map { it as Flow<Any> }

        @Suppress("UNCHECKED_CAST")
        return combine(flows) { array ->
            array.map { it as Entity }
                .applyFilters(request.filters)
                .applySorts(request.sorts)
                .applyOffset(request.offset)
                .applyLimit(request.limit)
                .map(mapper::toModel)
        }
    }

    override fun getAll(): Flow<List<Model>> {
        val flows: List<Flow<Any>> = items.values.map { it as Flow<Any> }

        @Suppress("UNCHECKED_CAST")
        return combine(flows) { array ->
            array
                .map { it as Entity }
                .map(mapper::toModel)
        }
    }
}