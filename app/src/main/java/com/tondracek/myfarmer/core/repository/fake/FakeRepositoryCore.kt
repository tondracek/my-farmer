package com.tondracek.myfarmer.core.repository.fake

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

class FakeRepositoryCore<Entity : RepositoryEntity<ID>, ID>() : RepositoryCore<Entity, ID> {

    val entities: MutableMap<ID, MutableStateFlow<Entity>> = mutableMapOf()

    override suspend fun create(entity: Entity): ID {
        val id = entity.id
        entities[id] = MutableStateFlow(entity)
        return id
    }

    override suspend fun update(entity: Entity) {
        val id = entity.id
        entities[id]?.value = entity
    }

    override suspend fun delete(id: ID) {
        entities.remove(id)
    }

    override fun getById(id: ID): Flow<Entity?> {
        return entities[id] ?: flowOf(null)
    }

    override fun get(request: RepositoryRequest): Flow<List<Entity>> {
        val flows: List<Flow<Any>> = entities.values.map { it as Flow<Any> }
        if (flows.isEmpty()) return flowOf(emptyList())

        @Suppress("UNCHECKED_CAST")
        return combine(flows) { array ->
            array.map { it as Entity }
                .applyFilters(request.filters)
                .applySorts(request.sorts)
                .applyOffset(request.offset)
                .applyLimit(request.limit)
        }
    }

    override fun getAll(): Flow<List<Entity>> {
        val flows: List<Flow<Any>> = entities.values.map { it as Flow<Any> }
        if (flows.isEmpty()) return flowOf(emptyList())

        @Suppress("UNCHECKED_CAST")
        return combine(flows) { array ->
            array.map { it as Entity }
        }
    }
}