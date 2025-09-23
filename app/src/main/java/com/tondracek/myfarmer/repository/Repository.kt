package com.tondracek.myfarmer.repository

import com.tondracek.myfarmer.repository.request.RepositoryRequest
import java.util.UUID

interface Repository<T> {

    suspend fun create(item: T): UUID

    suspend fun update(item: T): Boolean

    suspend fun getByID(id: UUID): T?

    suspend fun get(request: RepositoryRequest): List<T>

}