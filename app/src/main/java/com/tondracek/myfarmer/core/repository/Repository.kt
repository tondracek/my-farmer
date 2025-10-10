package com.tondracek.myfarmer.core.repository

import com.tondracek.myfarmer.core.repository.request.RepositoryRequest
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface Repository<T> {

    suspend fun create(item: T)

    suspend fun update(item: T): Boolean

    suspend fun delete(id: UUID): Boolean

    fun getByID(id: UUID): Flow<T?>

    fun get(request: RepositoryRequest): Flow<List<T>>

}