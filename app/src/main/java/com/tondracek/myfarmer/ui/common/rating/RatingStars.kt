package com.tondracek.myfarmer.ui.common.rating

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.review.domain.model.Rating
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

@Preview
@Composable
private fun RatingPreview() {
    MyFarmerTheme {
        Surface {
            RatingStars(
                rating = Rating(3)
            )
        }
    }
}