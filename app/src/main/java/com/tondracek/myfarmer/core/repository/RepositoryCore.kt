package com.tondracek.myfarmer.core.repository

import com.tondracek.myfarmer.core.repository.request.RepositoryRequest
import kotlinx.coroutines.flow.Flow

interface RepositoryCore<Entity : RepositoryEntity<ID>, ID> {

    suspend fun create(entity: Entity): ID

    suspend fun update(entity: Entity)

    suspend fun delete(id: ID)

    fun getById(id: ID): Flow<Entity?>

    fun get(request: RepositoryRequest): Flow<List<Entity>>

    fun getAll(): Flow<List<Entity>>
}