package com.tondracek.myfarmer.ui.mainshopscreen.shopslistview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.tondracek.myfarmer.location.model.km
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.shop.data.sampleShops
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.ui.common.layout.LoadingLayout
import com.tondracek.myfarmer.ui.common.paging.FullScreenLoading
import com.tondracek.myfarmer.ui.common.paging.LoadingMoreItem
import com.tondracek.myfarmer.ui.common.paging.RetryItem
import com.tondracek.myfarmer.ui.common.paging.collectAsLazyPagingItemsAndSnackbarErrors
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.preview.PreviewApi34
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.mainshopscreen.shopslistview.components.ShopListItemCard
import com.tondracek.myfarmer.ui.mainshopscreen.shopslistview.components.toListItem
import kotlinx.coroutines.flow.flowOf

@Composable
fun ShopsListView(
    modifier: Modifier = Modifier,
    state: ShopsListViewState,
    onNavigateToShopDetail: (ShopId) -> Unit,
    onNavigateBack: () -> Unit,
) {
    when (state) {
        is ShopsListViewState.Success -> SuccessLayout(
            modifier = modifier,
            state = state,
            onNavigateToShopDetail = onNavigateToShopDetail
        )

        ShopsListViewState.Loading -> LoadingLayout()
    }
}

@Composable
private fun SuccessLayout(
    modifier: Modifier,
    state: ShopsListViewState.Success,
    onNavigateToShopDetail: (ShopId) -> Unit
) {
    val lazyListState = rememberLazyListState()
    val pagingItems = state.shops.collectAsLazyPagingItemsAndSnackbarErrors()

    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        contentPadding = PaddingValues(
            horizontal = MyFarmerTheme.paddings.medium,
            vertical = MyFarmerTheme.paddings.topBarMargin
        ),
        verticalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.medium)
    ) {
        items(
            count = pagingItems.itemCount,
            key = { index -> pagingItems[index]?.id?.value ?: index }
        ) { index ->
            pagingItems[index]?.let { shop ->
                ShopListItemCard(
                    modifier = Modifier,
                    shop = shop,
                    onNavigateToShopDetail = onNavigateToShopDetail
                )
            }
        }

        when {
            pagingItems.loadState.refresh is LoadState.Loading -> {
                item { FullScreenLoading() }
            }

            pagingItems.loadState.append is LoadState.Loading -> {
                item { LoadingMoreItem() }
            }

            pagingItems.loadState.append is LoadState.Error -> {
                item { RetryItem { pagingItems.retry() } }
            }
        }
    }
}

@PreviewApi34
@Composable
private fun ShopsListViewPreview() {
    MyFarmerPreview {
        val shops = sampleShops.map {
            it.toListItem(
                distance = 2.5.km,
                averageRating = Rating.ZERO
            )
        }

        ShopsListView(
            modifier = Modifier,
            state = ShopsListViewState.Success(shops = flowOf(PagingData.from(shops))),
            onNavigateToShopDetail = {},
            onNavigateBack = {},
        )
    }
}