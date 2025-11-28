package com.tondracek.myfarmer.ui.shopdetailscreen.components.sections

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tondracek.myfarmer.ui.common.review.ReviewCard
import com.tondracek.myfarmer.ui.common.review.ReviewUiState
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.shopdetailscreen.components.sectionlayout.ShopDetailSectionLayout

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
        items(reviews) {
            ReviewCard(
                modifier = Modifier.padding(MyFarmerTheme.paddings.medium),
                review = it
            )
        }
    }
}
