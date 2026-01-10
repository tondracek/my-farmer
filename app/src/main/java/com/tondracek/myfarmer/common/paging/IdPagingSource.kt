package com.tondracek.myfarmer.common.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState

open class IdPagingSource<Id : Any, Data : Any>(
    private val getDataKey: suspend (data: Data) -> Id,
    private val getData: suspend (limit: Int, after: Id?) -> List<Data>
) : PagingSource<Id, Data>() {

    override suspend fun load(params: LoadParams<Id>): LoadResult<Id, Data> {
        return try {
            val data = getData(params.loadSize, params.key)

            LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = data.lastOrNull()?.let { getDataKey(it) }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Id, Data>): Id? = null
}
