package com.tondracek.myfarmer.ui.shopslistview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.location.model.km
import com.tondracek.myfarmer.shop.data.sampleShops
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.ui.common.layout.LoadingLayout
import com.tondracek.myfarmer.ui.core.preview.AsyncImagePreviewFix
import com.tondracek.myfarmer.ui.core.preview.PreviewApi34
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.shopslistview.components.ShopListItemCard
import com.tondracek.myfarmer.ui.shopslistview.components.ShopListViewItem
import com.tondracek.myfarmer.ui.shopslistview.components.toListItem

@Composable
fun ShopsListView(
    modifier: Modifier = Modifier,
    state: ShopsListViewState,
    onNavigateToShopDetail: (ShopId) -> Unit,
) {
    when (state) {
        ShopsListViewState.Loading -> LoadingLayout()
        is ShopsListViewState.Success -> Success(
            modifier = modifier,
            state = state,
            onNavigateToShopDetail = onNavigateToShopDetail
        )
    }
}

@Composable
private fun Success(
    modifier: Modifier,
    state: ShopsListViewState.Success,
    onNavigateToShopDetail: (ShopId) -> Unit
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(state.shops) { shop: ShopListViewItem ->
            ShopListItemCard(
                modifier = Modifier,
                shop = shop,
                onNavigateToShopDetail = onNavigateToShopDetail
            )
        }
    }
}

@PreviewApi34
@Composable
private fun ShopsListViewPreview() {
    MyFarmerTheme {
        AsyncImagePreviewFix {
            ShopsListView(
                modifier = Modifier,
                state = ShopsListViewState.Success(
                    shops = sampleShops.map { it.toListItem(2.5.km) }
                ),
                onNavigateToShopDetail = {},
            )
        }
    }
}