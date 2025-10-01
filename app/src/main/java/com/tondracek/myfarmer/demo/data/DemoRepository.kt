package com.tondracek.myfarmer.demo.data

import com.tondracek.myfarmer.core.repository.Repository
import com.tondracek.myfarmer.core.repository.request.RepositoryRequest
import com.tondracek.myfarmer.demo.domain.Demo
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface DemoRepository : Repository<Demo> {

    override suspend fun create(item: Demo)

    override suspend fun update(item: Demo): Boolean

    override suspend fun delete(id: UUID): Boolean

    override fun getByID(id: UUID): Flow<Demo?>

    override fun get(request: RepositoryRequest): Flow<List<Demo>>

    fun getFiltered(names: List<String>?, index: Int?): Flow<List<Demo>>
}