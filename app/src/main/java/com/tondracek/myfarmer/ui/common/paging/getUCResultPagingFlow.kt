package com.tondracek.myfarmer.ui.common.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tondracek.myfarmer.core.domain.domainerror.DomainError
import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import kotlinx.coroutines.flow.Flow

fun <Key : Any, Model : Any> getUCResultPageFlow(
    getDataKey: suspend (data: Model) -> Key,
    pageSize: Int = 20,
    enablePlaceholders: Boolean = false,
    showError: suspend (error: DomainError) -> Unit,
    getData: suspend (limit: Int, after: Key?) -> DomainResult<List<Model>>,
): Flow<PagingData<Model>> = Pager(
    config = PagingConfig(
        pageSize = pageSize,
        enablePlaceholders = enablePlaceholders
    ),
    pagingSourceFactory = {
        UCResultPagingSource(
            getData = { limit, after -> getData(limit, after) },
            getDataKey = getDataKey,
            showError = showError
        )
    }
).flow
