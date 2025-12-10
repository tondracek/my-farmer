package com.tondracek.myfarmer.ui.common.rating

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun RatingStars(
    modifier: Modifier = Modifier,
    starModifier: Modifier = Modifier.size(32.dp),
    rating: Rating,
) {
    Box(modifier) {
        Row {
            for (i in 1..5) {
                Box {
                    Icon(
                        modifier = starModifier,
                        imageVector = Icons.Default.StarBorder,
                        tint = MyFarmerTheme.colors.line,
                        contentDescription = null,
                    )
                    Icon(
                        modifier = starModifier.scale(0.9f),
                        imageVector = if (i <= rating.stars) Icons.Default.Star else Icons.Default.StarBorder,
                        tint = MyFarmerTheme.colors.ratingStars,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Composable
fun RatingStarsInput(
    modifier: Modifier = Modifier,
    starModifier: Modifier = Modifier.size(32.dp),
    rating: Rating,
    onRatingChange: (Rating) -> Unit,
) {
    Box(modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            for (i in 1..5) {
                Box {
                    Icon(
                        modifier = starModifier,
                        imageVector = Icons.Default.StarBorder,
                        tint = MyFarmerTheme.colors.line,
                        contentDescription = null,
                    )
                    IconButton(
                        modifier = starModifier.scale(0.9f),
                        onClick = { onRatingChange(Rating(i)) }
                    ) {
                        Icon(
                            imageVector = if (i <= rating.stars) Icons.Default.Star else Icons.Default.StarBorder,
                            tint = MyFarmerTheme.colors.ratingStars,
                            contentDescription = null,
                        )
                    }
                }
            }
            Button(onClick = { onRatingChange(Rating(0)) }) {
                Text(stringResource(R.string.clear))
            }
        }
    }
}

@Preview
@Composable
private fun RatingPreview() {
    MyFarmerPreview {
        RatingStars(
            rating = Rating(3)
        )
    }
}

@Preview
@Composable
private fun RatingInputPreview() {
    MyFarmerPreview {
        RatingStarsInput(
            rating = Rating(4),
            onRatingChange = {}
        )
    }
}