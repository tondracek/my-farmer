package com.tondracek.myfarmer.ui.common.layout.shopdetaillayout.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tondracek.myfarmer.ui.common.review.ReviewCard
import com.tondracek.myfarmer.ui.common.review.ReviewUiState

@Composable
fun ReviewsPreviewSection(
    modifier: Modifier,
    reviews: List<ReviewUiState>,
    onReviewsClick: () -> Unit
) {
    ShopDetailSectionLayout(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onReviewsClick() },
        title = "Reviews Preview",
    ) {
        reviews.map { ReviewCard(review = it) }
    }
}
