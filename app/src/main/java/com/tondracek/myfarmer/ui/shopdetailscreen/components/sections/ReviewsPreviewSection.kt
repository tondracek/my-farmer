package com.tondracek.myfarmer.ui.shopdetailscreen.components.sections

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.tondracek.myfarmer.R
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
        title = stringResource(R.string.reviews_preview),
    ) {
        items(reviews) {
            ReviewCard(
                modifier = Modifier.padding(MyFarmerTheme.paddings.medium),
                review = it
            )
        }

        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MyFarmerTheme.paddings.medium),
                text = stringResource(R.string.see_all_reviews),
                style = MyFarmerTheme.typography.textMedium,
                textAlign = TextAlign.End
            )
        }
    }
}
