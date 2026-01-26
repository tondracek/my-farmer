package com.tondracek.myfarmer.ui.mainshopscreen.shopslistview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.location.model.km
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.shop.data.sampleShops
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.ui.common.button.RefreshIconButton
import com.tondracek.myfarmer.ui.common.layout.LoadingLayout
import com.tondracek.myfarmer.ui.common.paging.isLoading
import com.tondracek.myfarmer.ui.common.paging.paginatedItems
import com.tondracek.myfarmer.ui.common.scaffold.ScreenScaffold
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.preview.PreviewApi34
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.mainshopscreen.shopslistview.components.ShopListItemCard
import com.tondracek.myfarmer.ui.mainshopscreen.shopslistview.components.ShopListViewItem
import com.tondracek.myfarmer.ui.mainshopscreen.shopslistview.components.toListItem
import kotlinx.coroutines.flow.flowOf

@Composable
fun ShopsListView(
    modifier: Modifier = Modifier,
    state: ShopsListViewState,
    onNavigateToShopDetail: (ShopId) -> Unit,
    onRefreshClick: () -> Unit,
) {
    when (state) {
        is ShopsListViewState.Success -> SuccessLayout(
            modifier = modifier,
            state = state,
            onNavigateToShopDetail = onNavigateToShopDetail,
            onRefreshClick = onRefreshClick,
        )

        ShopsListViewState.Loading -> ScreenScaffold(
            title = stringResource(R.string.shops_list),
            applyTopBarPadding = false,
        ) {
            LoadingLayout(modifier = modifier)
        }
    }
}

@Composable
private fun SuccessLayout(
    modifier: Modifier,
    state: ShopsListViewState.Success,
    onNavigateToShopDetail: (ShopId) -> Unit,
    onRefreshClick: () -> Unit,
) {
    val pagingItems = state.shopsPaging.collectAsLazyPagingItems()
    val isRefreshing = pagingItems.isLoading()

    ScreenScaffold(
        title = stringResource(R.string.shops_list),
        applyTopBarPadding = false,
        rightIconContent = {
            RefreshIconButton(
                isRefreshing = isRefreshing,
                onClick = onRefreshClick,
            )
        },
    ) {
        ShopsList(
            modifier = modifier,
            pagingItems = pagingItems,
            onNavigateToShopDetail = onNavigateToShopDetail,
        )
    }
}

@Composable
private fun ShopsList(
    modifier: Modifier,
    pagingItems: LazyPagingItems<ShopListViewItem>,
    onNavigateToShopDetail: (ShopId) -> Unit,
) {
    val listState = rememberLazyListState()

    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = PaddingValues(
            horizontal = MyFarmerTheme.paddings.medium,
            vertical = MyFarmerTheme.paddings.topBarMargin
        ),
        verticalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.medium)
    ) {
        paginatedItems(
            pagingItems = pagingItems,
            getKey = { it.id },
        ) {
            ShopListItemCard(
                modifier = Modifier,
                shop = it,
                onNavigateToShopDetail = onNavigateToShopDetail
            )
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
            state = ShopsListViewState.Success(shopsPaging = flowOf(PagingData.from(shops))),
            onNavigateToShopDetail = {},
            onRefreshClick = {},
        )
    }
}