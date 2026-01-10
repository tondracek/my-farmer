package com.tondracek.myfarmer.ui.common.paging

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

fun <T : Any> LazyListScope.paginatedItems(
    pagingItems: LazyPagingItems<T>,
    getKey: (index: Int) -> Any,
    content: @Composable (item: T) -> Unit,
) {
    items(
        count = pagingItems.itemCount,
        key = getKey,
    ) { index ->
        pagingItems[index]?.let { item ->
            content(item)
        }
    }

    when {
        pagingItems.loadState.append is LoadState.Loading -> {
            item { LoadingMoreItem() }
        }

        pagingItems.loadState.refresh is LoadState.Loading -> {
            item { FullScreenLoading() }
        }

        pagingItems.loadState.append is LoadState.Error -> {
            item { RetryItem { pagingItems.retry() } }
        }
    }
}