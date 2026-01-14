package com.tondracek.myfarmer.core.domain.repository

import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import kotlinx.coroutines.flow.Flow

/**
 * Generic repository interface for CRUD operations.
 *
 * Provides:
 *  - Create
 *  - Update
 *  - Delete
 *  - Get by ID
 *  - Get with filtering and pagination
 *
 * @param Model The type of the domain model managed by the repository.
 */
interface Repository<Model, ModelId> {

    suspend fun create(item: Model): DomainResult<ModelId>

    suspend fun update(item: Model): DomainResult<Unit>

    suspend fun delete(id: ModelId): DomainResult<Unit>

    fun getById(id: ModelId): Flow<DomainResult<Model>>

    fun getAll(): Flow<DomainResult<List<Model>>>
}