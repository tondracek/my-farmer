package com.tondracek.myfarmer.ui.common.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult
import com.tondracek.myfarmer.core.domain.usecaseresult.toException

class UCResultPagingSource<Id : Any, Data : Any>(
    private val getDataKey: suspend (data: Data) -> Id,
    private val getData: suspend (limit: Int, after: Id?) -> UCResult<List<Data>>
) : PagingSource<Id, Data>() {

    override suspend fun load(params: LoadParams<Id>): LoadResult<Id, Data> {
        val result = getData(params.loadSize, params.key)

        return when (result) {
            is UCResult.Success -> LoadResult.Page(
                data = result.data,
                prevKey = null,
                nextKey = result.data.lastOrNull()?.let { getDataKey(it) }
            )

            is UCResult.Failure -> LoadResult.Error(result.toException())
        }
    }

    override fun getRefreshKey(state: PagingState<Id, Data>): Id? = null
}