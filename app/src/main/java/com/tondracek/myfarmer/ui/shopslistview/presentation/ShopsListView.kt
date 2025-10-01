package com.tondracek.myfarmer.ui.shopslistview.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.ui.shopslistview.presentation.components.ShopListItemCard
import com.tondracek.myfarmer.ui.shopslistview.presentation.model.ShopListViewItem
import com.tondracek.myfarmer.ui.shopslistview.presentation.model.toListItem
import com.tondracek.myfarmer.shared.domain.model.ShopId
import com.tondracek.myfarmer.shared.domain.model.sampleShops
import com.tondracek.myfarmer.shared.location.km
import com.tondracek.myfarmer.shared.theme.MyFarmerTheme
import com.tondracek.myfarmer.shared.ui.layout.LoadingScreen
import com.tondracek.myfarmer.shared.ui.preview.AsyncImagePreviewFix
import com.tondracek.myfarmer.shared.ui.preview.PreviewApi34

@Composable
fun ShopsListView(
    modifier: Modifier = Modifier,
    state: ShopsListViewState,
    onNavigateToShopDetail: (ShopId) -> Unit,
) {
    when (state) {
        ShopsListViewState.Loading -> LoadingScreen()
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

    Surface {
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