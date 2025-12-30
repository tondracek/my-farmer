package com.tondracek.myfarmer.ui.shopfilterdialog.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.ui.common.rating.RatingStarsInput

@Composable
internal fun RatingSection(
    modifier: Modifier = Modifier,
    selectedMinRating: Rating,
    onRatingChange: (Rating) -> Unit
) {
    RatingStarsInput(
        modifier = modifier,
        rating = selectedMinRating,
        onRatingChange = onRatingChange
    )
}