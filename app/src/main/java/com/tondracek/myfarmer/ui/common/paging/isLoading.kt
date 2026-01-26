package com.tondracek.myfarmer.ui.common.paging

import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

fun <T : Any> LazyPagingItems<T>.isLoading(): Boolean =
    this.loadState.refresh is LoadState.Loading
            || this.loadState.append is LoadState.Loading
            || this.loadState.prepend is LoadState.Loading
