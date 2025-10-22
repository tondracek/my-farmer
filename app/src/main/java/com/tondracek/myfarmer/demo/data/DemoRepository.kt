package com.tondracek.myfarmer.demo.data

import com.tondracek.myfarmer.core.repository.Repository
import com.tondracek.myfarmer.demo.domain.Demo
import kotlinx.coroutines.flow.Flow

interface DemoRepository : Repository<Demo> {

    fun getFiltered(names: List<String>?, index: Int?): Flow<List<Demo>>
}