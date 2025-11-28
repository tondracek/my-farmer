package com.tondracek.myfarmer.ui.common.review

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tondracek.myfarmer.shop.data.sampleReviews
import com.tondracek.myfarmer.systemuser.data.sampleUsers
import com.tondracek.myfarmer.ui.common.rating.RatingStars
import com.tondracek.myfarmer.ui.common.user.UserPreviewCard
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun ReviewCard(
    modifier: Modifier = Modifier,
    review: ReviewUiState,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = MyFarmerTheme.cardColors.primary
    ) {
        Column(modifier = Modifier.padding(MyFarmerTheme.paddings.small)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                UserPreviewCard(user = review.author)

                RatingStars(rating = review.rating)
            }

            if (!review.comment.isNullOrBlank())
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = review.comment,
                    style = MyFarmerTheme.typography.textMedium
                )
        }
    }
}

@Preview
@Composable
private fun ReviewCardPreview0() {
    val review = sampleReviews.first { it.comment != null }
    val author = sampleUsers.first { it.id == review.userId }

    MyFarmerPreview {
        ReviewCard(
            review = review.toUiState(author = author)
        )
    }
}

@Preview
@Composable
private fun ReviewCardPreview1() {
    val review = sampleReviews.first { it.comment.isNullOrBlank() }
    val author = sampleUsers.first { it.id == review.userId }

    MyFarmerPreview {
        ReviewCard(
            review = review.toUiState(author = author)
        )
    }
}