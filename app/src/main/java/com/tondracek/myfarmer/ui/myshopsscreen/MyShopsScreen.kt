package com.tondracek.myfarmer.ui.myshopsscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tondracek.myfarmer.auth.domain.usecase.result.NotLoggedInUCResult
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.shop.data.sampleShops
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.ui.common.layout.ErrorLayout
import com.tondracek.myfarmer.ui.common.layout.LoadingLayout
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.myshopsscreen.components.MyShopsListItemView

@Composable
fun MyShopsScreen(
    modifier: Modifier = Modifier,
    state: MyShopsState,
    onShopClick: (shopId: ShopId) -> Unit,
    onUpdateShopClick: (shopId: ShopId) -> Unit,
    onDeleteShopClick: (shopId: ShopId) -> Unit,
    onCreateShopClick: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    when (state) {
        is MyShopsState.Success -> SuccessScreen(
            modifier = modifier,
            shops = state.shops,
            onShopClick = onShopClick,
            onUpdateShopClick = onUpdateShopClick,
            onDeleteShopClick = onDeleteShopClick,
            onCreateShopClick = onCreateShopClick
        )

        is MyShopsState.Loading -> LoadingLayout(modifier = modifier)
        is MyShopsState.Error -> ErrorLayout(
            failure = state.failure,
            onNavigateBack = onNavigateBack
        )
    }
}

@Composable
private fun SuccessScreen(
    modifier: Modifier = Modifier,
    shops: List<MyShopsListItem>,
    onShopClick: (shopId: ShopId) -> Unit,
    onUpdateShopClick: (shopId: ShopId) -> Unit,
    onDeleteShopClick: (shopId: ShopId) -> Unit,
    onCreateShopClick: () -> Unit,
) {
    Box(modifier = modifier) {
        val lazyListState = rememberLazyListState()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState,
            contentPadding = PaddingValues(
                vertical = MyFarmerTheme.paddings.topBarMargin,
                horizontal = MyFarmerTheme.paddings.medium,
            ),
            verticalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.medium)
        ) {
            items(shops) { shop ->
                MyShopsListItemView(
                    shop = shop,
                    onShopClick = onShopClick,
                    onUpdateShopClick = onUpdateShopClick,
                    onDeleteShopClick = onDeleteShopClick
                )
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .align(androidx.compose.ui.Alignment.BottomCenter)
                .padding(MyFarmerTheme.paddings.bottomButtons),
            onClick = onCreateShopClick,
        ) {
            Text(text = "Create Shop")
        }
    }
}

@Preview
@Composable
private fun MyShopsScreenSuccessPreview() {
    MyFarmerPreview {
        MyShopsScreen(
            state = MyShopsState.Success(sampleShops.map { it.toMyShopsListItem(Rating.ZERO) }),
            onShopClick = {},
            onUpdateShopClick = {},
            onDeleteShopClick = {},
            onCreateShopClick = {},
            onNavigateBack = {},
        )
    }
}

@Preview
@Composable
private fun MyShopsScreenLoadingPreview() {
    MyFarmerPreview {
        MyShopsScreen(
            state = MyShopsState.Loading,
            onShopClick = {},
            onUpdateShopClick = {},
            onDeleteShopClick = {},
            onCreateShopClick = {},
            onNavigateBack = {},
        )
    }
}

@Preview
@Composable
private fun MyShopsScreenErrorPreview() {
    MyFarmerPreview {
        MyShopsScreen(
            state = MyShopsState.Error(NotLoggedInUCResult()),
            onShopClick = {},
            onUpdateShopClick = {},
            onDeleteShopClick = {},
            onCreateShopClick = {},
            onNavigateBack = {},
        )
    }
}