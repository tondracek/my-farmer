package com.tondracek.myfarmer.ui.reviewscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.review.domain.model.ReviewInput
import com.tondracek.myfarmer.shop.data.sampleReviewsUI
import com.tondracek.myfarmer.ui.common.layout.ErrorLayout
import com.tondracek.myfarmer.ui.common.layout.LoadingLayout
import com.tondracek.myfarmer.ui.common.review.ReviewCard
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.reviewscreen.components.CreateShopReviewBottomSheet

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
    val density = LocalDensity.current

    Box(modifier = Modifier.fillMaxSize()) {
        var verticalPadding by remember { mutableStateOf(128.dp) }

        LazyColumn(
            modifier = Modifier.padding(horizontal = MyFarmerTheme.paddings.medium),
            state = rememberLazyListState(),
            contentPadding = PaddingValues(vertical = verticalPadding),
            verticalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.small)
        ) {
            items(state.reviews) { review ->
                ReviewCard(
                    review = review,
                    colors = MyFarmerTheme.cardColors.secondary,
                )
            }
        }

        state.shopName?.let {
            Box(modifier = Modifier.onGloballyPositioned {
                verticalPadding = with(density) { (it.size.height.toDp()) }
            }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MyFarmerTheme.paddings.small)
                        .align(Alignment.TopCenter),
                    colors = MyFarmerTheme.cardColors.primary,
                    shape = RoundedCornerShape(32.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MyFarmerTheme.paddings.large),
                        text = state.shopName,
                        textAlign = TextAlign.Center,
                        style = MyFarmerTheme.typography.titleMedium,
                    )
                }
            }
        }

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

@Preview
@Composable
private fun ShopReviewsScreenPreview() {
    MyFarmerPreview {
        ShopReviewsScreen(
            state = ShopReviewsScreenState.Success(
                shopName = "Sample Shop long long long long long long long long long long long long name",
                reviews = sampleReviewsUI
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
                reviews = listOf(sampleReviewsUI.first())
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
                reviews = emptyList()
            ),
            onSubmitReview = {},
            onBackClick = {}
        )
    }
}


