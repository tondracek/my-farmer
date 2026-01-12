package com.tondracek.myfarmer.core.domain.repository

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

    suspend fun create(item: Model): ModelId

    suspend fun update(item: Model)

    suspend fun delete(id: ModelId)

    fun getById(id: ModelId): Flow<Model?>

    fun getAll(): Flow<List<Model>>
}