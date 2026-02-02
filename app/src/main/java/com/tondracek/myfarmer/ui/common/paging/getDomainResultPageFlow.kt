package com.tondracek.myfarmer.ui.common.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tondracek.myfarmer.core.domain.domainerror.DomainError
import com.tondracek.myfarmer.core.domain.domainresult.DomainResult
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

fun <Item : Any, Cursor> getDomainResultPageFlow(
    pageSize: Int = 20,
    showError: suspend (error: DomainError) -> Unit,
    getItems: suspend (pageSize: Int, cursor: Cursor?) -> DomainResult<Pair<List<Item>, Cursor?>>,
): Flow<PagingData<Item>> = Pager(
    config = PagingConfig(
        pageSize = pageSize,
        enablePlaceholders = false,
    ),
    pagingSourceFactory = {
        DomainResultPagingSource(
            pageSize = pageSize,
            showError = showError,
            getItems = getItems,
        )
    }
).flow


private class DomainResultPagingSource<Item : Any, Cursor : Any>(
    private val pageSize: Int,
    private val showError: suspend (error: DomainError) -> Unit,
    private val getItems: suspend (pageSize: Int, cursor: Cursor?) -> DomainResult<Pair<List<Item>, Cursor?>>,
) : PagingSource<Cursor, Item>() {

    override fun getRefreshKey(state: PagingState<Cursor, Item>) = null

    override suspend fun load(params: LoadParams<Cursor>): LoadResult<Cursor, Item> =
        when (val result = getItems(pageSize, params.key)) {
            is DomainResult.Success<Pair<List<Item>, Cursor?>> -> {
                val (items, nextCursor) = result.data
                Timber.d("Loaded page with key=${params.key}, items=${items.size}, nextKey=$nextCursor")
                LoadResult.Page(
                    data = items,
                    prevKey = null,
                    nextKey = nextCursor,
                )
            }

            is DomainResult.Failure -> result
                .also { Timber.e("Failed to load page with key=${params.key}, error=${result.error}") }
                .withFailure { showError(it.error) }
                .let { LoadResult.Error(result.cause ?: Exception()) }
        }
}