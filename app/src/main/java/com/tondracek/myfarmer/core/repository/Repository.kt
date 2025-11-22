package com.tondracek.myfarmer.core.repository

import com.tondracek.myfarmer.core.repository.request.RepositoryRequest
import kotlinx.coroutines.flow.Flow
import java.util.UUID

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
interface Repository<Model> {

    suspend fun create(item: Model): UUID

    suspend fun update(item: Model)

    suspend fun delete(id: UUID)

    fun getById(id: UUID): Flow<Model?>

    fun get(request: RepositoryRequest): Flow<List<Model>>

}