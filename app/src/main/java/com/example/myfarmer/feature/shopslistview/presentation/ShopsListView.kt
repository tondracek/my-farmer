package com.example.myfarmer.feature.shopslistview.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myfarmer.feature.shopslistview.presentation.components.ShopListItemCard
import com.example.myfarmer.feature.shopslistview.presentation.model.ShopListViewItem
import com.example.myfarmer.feature.shopslistview.presentation.model.toListItem
import com.example.myfarmer.shared.domain.ShopId
import com.example.myfarmer.shared.domain.sampleShops
import com.example.myfarmer.shared.theme.MyFarmerTheme
import com.example.myfarmer.shared.ui.preview.AsyncImagePreviewFix
import com.example.myfarmer.shared.ui.preview.PreviewApi34

@Composable
fun ShopsListView(
    modifier: Modifier = Modifier,
    state: ShopsListViewState,
    onNavigateToShopDetail: (ShopId) -> Unit,
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
                state = ShopsListViewState(
                    shops = sampleShops.map { it.toListItem() }
                ),
                onNavigateToShopDetail = {},
            )
        }
    }
}