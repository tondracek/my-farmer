package com.tondracek.myfarmer.ui.mainshopscreen.shopslistview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tondracek.myfarmer.location.model.km
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.shop.data.sampleShops
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.ui.common.layout.ErrorLayout
import com.tondracek.myfarmer.ui.common.layout.LoadingLayout
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.preview.PreviewApi34
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.mainshopscreen.shopslistview.components.ShopListItemCard
import com.tondracek.myfarmer.ui.mainshopscreen.shopslistview.components.ShopListViewItem
import com.tondracek.myfarmer.ui.mainshopscreen.shopslistview.components.toListItem

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
        is ShopsListViewState.Error -> ErrorLayout(
            failure = state.error,
            onNavigateBack = onNavigateBack
        )
    }
}

@Composable
private fun SuccessLayout(
    modifier: Modifier,
    state: ShopsListViewState.Success,
    onNavigateToShopDetail: (ShopId) -> Unit
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        contentPadding = PaddingValues(
            horizontal = MyFarmerTheme.paddings.medium,
            vertical = MyFarmerTheme.paddings.topBarMargin
        ),
        verticalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.medium)
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
    MyFarmerPreview {
        val shops = sampleShops.map {
            it.toListItem(
                distance = 2.5.km,
                averageRating = Rating.ZERO
            )
        }

        ShopsListView(
            modifier = Modifier,
            state = ShopsListViewState.Success(shops = shops),
            onNavigateToShopDetail = {},
            onNavigateBack = {},
        )
    }
}