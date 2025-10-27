package com.tondracek.myfarmer.core.repository

import com.tondracek.myfarmer.core.repository.request.RepositoryRequest
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface RepositoryCore<Model> {

    suspend fun create(item: Model)

    suspend fun update(item: Model): Boolean

    suspend fun delete(id: UUID): Boolean

    fun getById(id: UUID): Flow<Model?>

    fun get(request: RepositoryRequest): Flow<List<Model>>
}