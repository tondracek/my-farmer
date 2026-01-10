package com.tondracek.myfarmer.ui.common.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import kotlinx.coroutines.flow.Flow

fun <Id : Any, Model : Any> getUCResultPageDataFlow(
    getDataKey: suspend (data: Model) -> Id,
    pageSize: Int = 20,
    enablePlaceholders: Boolean = false,
    getData: suspend (limit: Int, after: Id?) -> UCResult<List<Model>>,
): Flow<PagingData<Model>> = Pager(
    config = PagingConfig(
        pageSize = pageSize,
        enablePlaceholders = enablePlaceholders
    ),
    pagingSourceFactory = {
        UCResultPagingSource(
            getData = { limit, after -> getData(limit, after) },
            getDataKey = getDataKey,
        )
    }
).flow
