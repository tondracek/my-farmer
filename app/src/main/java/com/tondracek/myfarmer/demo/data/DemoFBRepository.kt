package com.tondracek.myfarmer.demo.data

import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.core.repository.firebase.FirebaseRepository
import com.tondracek.myfarmer.core.repository.request.AscendingSort
import com.tondracek.myfarmer.core.repository.request.DescendingSort
import com.tondracek.myfarmer.core.repository.request.FilterEq
import com.tondracek.myfarmer.core.repository.request.FilterIn
import com.tondracek.myfarmer.core.repository.request.RepositoryRequest
import com.tondracek.myfarmer.demo.domain.Demo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DemoFBRepository @Inject constructor() : DemoRepository,
    FirebaseRepository<Demo, DemoEntity>(DemoEntity::class.java) {

    override val mapper: EntityMapper<Demo, DemoEntity> = DemoEntityMapper

    override fun getFiltered(names: List<String>?, index: Int?): Flow<List<Demo>> {
        val request = RepositoryRequest.Builder()
            .addFilters(
                names?.let { FilterIn(field = DemoEntity::name, values = names) },
                index?.let { FilterEq(field = DemoEntity::index, value = index) }
            )
            .addSorts(
                DescendingSort(field = DemoEntity::index),
                AscendingSort(field = DemoEntity::name)
            )
            .build()

        return get(request)
    }
}
