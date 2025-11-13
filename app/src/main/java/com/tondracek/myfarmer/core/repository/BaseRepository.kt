package com.tondracek.myfarmer.core.repository

import com.tondracek.myfarmer.core.repository.request.RepositoryRequest
import kotlinx.coroutines.flow.Flow
import java.util.UUID

abstract class BaseRepository<Model>(
    private val core: RepositoryCore<Model>
) : Repository<Model> {

    override suspend fun create(item: Model): UUID = core.create(item)

    override suspend fun update(item: Model): Boolean = core.update(item)

    override suspend fun delete(id: UUID): Boolean = core.delete(id)

    override fun getById(id: UUID): Flow<Model?> = core.getById(id)

    override fun get(request: RepositoryRequest): Flow<List<Model>> = core.get(request)
}