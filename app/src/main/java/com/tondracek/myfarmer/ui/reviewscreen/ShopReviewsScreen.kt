package com.tondracek.myfarmer.ui.reviewscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.review.domain.model.ReviewInput
import com.tondracek.myfarmer.shop.data.sampleReviewsUI
import com.tondracek.myfarmer.ui.common.layout.ErrorLayout
import com.tondracek.myfarmer.ui.common.layout.LoadingLayout
import com.tondracek.myfarmer.ui.common.paging.FullScreenLoading
import com.tondracek.myfarmer.ui.common.paging.LoadingMoreItem
import com.tondracek.myfarmer.ui.common.paging.RetryItem
import com.tondracek.myfarmer.ui.common.paging.collectAsLazyPagingItemsAndSnackbarErrors
import com.tondracek.myfarmer.ui.common.review.ReviewCard
import com.tondracek.myfarmer.ui.common.scaffold.ScreenScaffold
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.reviewscreen.components.CreateShopReviewBottomSheet
import kotlinx.coroutines.flow.flowOf

@Composable
fun ShopReviewsScreen(
    state: ShopReviewsScreenState,
    onSubmitReview: (reviewInput: ReviewInput) -> Unit,
    onBackClick: () -> Unit,
) {
    when (state) {
        is ShopReviewsScreenState.Success -> Content(
            state = state,
            onSubmitReview = onSubmitReview
        )

        is ShopReviewsScreenState.Loading -> LoadingLayout()

        is ShopReviewsScreenState.Error -> ErrorLayout(
            failure = state.failure,
            onNavigateBack = onBackClick
        )
    }
}

@Composable
private fun Content(
    state: ShopReviewsScreenState.Success,
    onSubmitReview: (reviewInput: ReviewInput) -> Unit,
) {
    var showNewReviewBottomSheet by remember { mutableStateOf(false) }

    ScreenScaffold(
        title = state.shopName ?: stringResource(R.string.app_name)
    ) {

        ReviewsList(state)

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MyFarmerTheme.paddings.bottomButtons)
                .align(Alignment.BottomCenter),
            onClick = { showNewReviewBottomSheet = true }
        ) {
            Text("Write a review")
        }
    }

    if (showNewReviewBottomSheet) {
        CreateShopReviewBottomSheet(
            onDismissRequest = { showNewReviewBottomSheet = false },
            onSubmitReview = { reviewInput -> onSubmitReview(reviewInput) }
        )
    }
}

@Composable
private fun ReviewsList(state: ShopReviewsScreenState.Success) {
    val pagingItems = state.reviews.collectAsLazyPagingItemsAndSnackbarErrors()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = MyFarmerTheme.paddings.medium),
        state = rememberLazyListState(),
        contentPadding = PaddingValues(bottom = MyFarmerTheme.paddings.xxL),
        verticalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.small)
    ) {
        items(
            count = pagingItems.itemCount,
            key = { index -> pagingItems[index]?.id?.value ?: index }
        ) { index ->
            pagingItems[index]?.let { review ->
                ReviewCard(
                    review = review,
                    colors = MyFarmerTheme.cardColors.secondary,
                )
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
}

@Preview
@Composable
private fun ShopReviewsScreenPreview() {
    MyFarmerPreview {
        ShopReviewsScreen(
            state = ShopReviewsScreenState.Success(
                shopName = "Sample Shop long long long long long long long long long long long long name",
                reviews = flowOf(PagingData.from(sampleReviewsUI))
            ),
            onSubmitReview = {},
            onBackClick = {}
        )
    }
}

@Preview
@Composable
private fun ShopReviewsScreenPreviewSingleReview() {
    MyFarmerPreview {
        ShopReviewsScreen(
            state = ShopReviewsScreenState.Success(
                shopName = "Sample Shop",
                reviews = flowOf(PagingData.from(listOf(sampleReviewsUI.first())))
            ),
            onSubmitReview = {},
            onBackClick = {}
        )
    }
}

@Preview
@Composable
private fun ShopReviewsScreenPreviewNoReview() {
    MyFarmerPreview {
        ShopReviewsScreen(
            state = ShopReviewsScreenState.Success(
                shopName = "Sample Shop",
                reviews = flowOf(PagingData.from(emptyList()))
            ),
            onSubmitReview = {},
            onBackClick = {}
        )
    }
}


