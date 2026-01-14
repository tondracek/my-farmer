package com.tondracek.myfarmer.ui.reviewscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.review.domain.model.ReviewId
import com.tondracek.myfarmer.review.domain.model.ReviewInput
import com.tondracek.myfarmer.shop.data.sampleReviewsUI
import com.tondracek.myfarmer.ui.common.divider.CustomHorizontalDivider
import com.tondracek.myfarmer.ui.common.layout.LoadingLayout
import com.tondracek.myfarmer.ui.common.paging.paginatedItems
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
    onReviewDeleteClick: (ReviewId) -> Unit,
) {
    when (state) {
        is ShopReviewsScreenState.Success -> Content(
            state = state,
            onSubmitReview = onSubmitReview,
            onReviewDeleteClick = onReviewDeleteClick,
        )

        is ShopReviewsScreenState.Loading -> LoadingLayout()
    }
}

@Composable
private fun Content(
    state: ShopReviewsScreenState.Success,
    onSubmitReview: (reviewInput: ReviewInput) -> Unit,
    onReviewDeleteClick: (ReviewId) -> Unit,
) {
    var showNewReviewBottomSheet by remember { mutableStateOf(false) }

    ScreenScaffold(
        title = state.shopName ?: stringResource(R.string.app_name)
    ) {

        ReviewsList(
            state = state,
            onReviewDeleteClick = onReviewDeleteClick,
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MyFarmerTheme.paddings.bottomButtons)
                .align(Alignment.BottomCenter),
            onClick = { showNewReviewBottomSheet = true },
            enabled = state.isLoggedIn && state.myReview == null
        ) {
            Text(
                when {
                    !state.isLoggedIn -> stringResource(R.string.you_must_be_logged_in_to_write_a_review)
                    state.myReview != null -> stringResource(R.string.you_have_already_written_a_review)
                    else -> stringResource(R.string.write_a_review)
                }
            )
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
private fun ReviewsList(
    state: ShopReviewsScreenState.Success,
    onReviewDeleteClick: (ReviewId) -> Unit,
) {
    val pagingItems = state.reviews.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = MyFarmerTheme.paddings.medium),
        state = rememberLazyListState(),
        contentPadding = PaddingValues(bottom = MyFarmerTheme.paddings.xxL),
        verticalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.small)
    ) {
        item {
            if (state.myReview != null)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.small)
                ) {
                    CustomHorizontalDivider()
                    Text(stringResource(R.string.my_review))
                    ReviewCard(
                        review = state.myReview,
                        colors = MyFarmerTheme.cardColors.primary,
                        onDeleteClick = { onReviewDeleteClick(state.myReview.id) }
                    )
                    Spacer(Modifier.size(MyFarmerTheme.paddings.medium))
                    CustomHorizontalDivider()
                    Text(stringResource(R.string.other_reviews))
                }
        }

        paginatedItems(
            pagingItems = pagingItems,
            getKey = { it.id.value }
        ) {
            ReviewCard(
                review = it,
                colors = MyFarmerTheme.cardColors.secondary,
            )
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
                isLoggedIn = true,
                myReview = sampleReviewsUI.first(),
                reviews = flowOf(PagingData.from(sampleReviewsUI))
            ),
            onSubmitReview = {},
            onReviewDeleteClick = {},
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
                isLoggedIn = true,
                myReview = null,
                reviews = flowOf(PagingData.from(listOf(sampleReviewsUI.first())))
            ),
            onSubmitReview = {},
            onReviewDeleteClick = {},
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
                isLoggedIn = false,
                myReview = null,
                reviews = flowOf(PagingData.from(emptyList()))
            ),
            onSubmitReview = {},
            onReviewDeleteClick = {},
        )
    }
}


