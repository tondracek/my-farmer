package com.tondracek.myfarmer.ui.common.paging

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.tondracek.myfarmer.ui.core.appstate.LocalAppUiController
import kotlinx.coroutines.flow.Flow

@Composable
fun <T : Any> Flow<PagingData<T>>.collectAsLazyPagingItemsAndSnackbarErrors(): LazyPagingItems<T> {
    val appUiController = LocalAppUiController.current

    val pagingItems = this.collectAsLazyPagingItems()

    LaunchedEffect(pagingItems.loadState) {
        val error = when {
            pagingItems.loadState.refresh is LoadState.Error ->
                (pagingItems.loadState.refresh as LoadState.Error).error

            pagingItems.loadState.append is LoadState.Error ->
                (pagingItems.loadState.append as LoadState.Error).error

            pagingItems.loadState.prepend is LoadState.Error ->
                (pagingItems.loadState.prepend as LoadState.Error).error


            else -> null
        }

        error?.let { exception ->
            exception.message?.let {
                appUiController.showErrorMessage(it)
            }
        }
    }

    return pagingItems
}
