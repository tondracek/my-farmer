package com.tondracek.myfarmer.ui.reviewscreen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.model.ReviewInput
import com.tondracek.myfarmer.ui.common.button.ButtonRow
import com.tondracek.myfarmer.ui.common.rating.RatingStarsInput
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateShopReviewBottomSheet(
    onDismissRequest: () -> Unit,
    onSubmitReview: (ReviewInput) -> Unit,
) {
    var rating by remember { mutableStateOf(Rating(0)) }
    var message by remember { mutableStateOf("") }

    Content(
        rating = rating,
        onRatingChange = { rating = it },
        message = message,
        onMessageChange = { message = it },
        onSubmitReview = onSubmitReview,
        onDismissRequest = onDismissRequest,
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun Content(
    rating: Rating,
    onRatingChange: (Rating) -> Unit,
    message: String,
    onMessageChange: (String) -> Unit,
    onSubmitReview: (ReviewInput) -> Unit,
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            RatingStarsInput(
                modifier = Modifier.padding(MyFarmerTheme.paddings.medium),
                rating = rating,
                onRatingChange = onRatingChange,
            )

            TextField(
                modifier = Modifier.padding(MyFarmerTheme.paddings.medium),
                value = message,
                onValueChange = onMessageChange,
                label = { Text("Your review") },
                minLines = 5,
                maxLines = 10,
            )

            ButtonRow(
                onClick1 = onDismissRequest,
                text1 = stringResource(R.string.close),
                onClick2 = {
                    onSubmitReview(ReviewInput(rating, message))
                    onDismissRequest()
                },
                text2 = stringResource(R.string.submit),
            )
        }
    }
}

@Preview
@Composable
private fun CreateShopReviewBottomSheetPreview() {
    MyFarmerPreview {
        Content(
            rating = Rating(4),
            onRatingChange = {},
            message = "Great shop with fresh produce!",
            onMessageChange = {},
            onDismissRequest = {},
            onSubmitReview = {},
        )
    }
}