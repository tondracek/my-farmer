package com.tondracek.myfarmer.ui.common.review

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.auth.domain.model.AuthId
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.contactinfo.domain.model.ContactInfo
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.model.ReviewId
import com.tondracek.myfarmer.shop.data.sampleReviews
import com.tondracek.myfarmer.systemuser.data.sampleUsers
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import com.tondracek.myfarmer.systemuser.domain.model.UserId
import com.tondracek.myfarmer.ui.common.button.DeleteButton
import com.tondracek.myfarmer.ui.common.rating.RatingStars
import com.tondracek.myfarmer.ui.common.user.UserPreviewCard
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@Composable
fun Instant?.toLocalDate(): String? {
    if (this == null) return null

    val formatter = DateTimeFormatter
        .ofLocalizedDate(FormatStyle.MEDIUM)
        .withLocale(Locale.getDefault())

    val zone = ZoneId.systemDefault()

    return LocalDateTime.ofInstant(this, zone)
        .toLocalDate()
        .format(formatter)
}

@Composable
fun ReviewCard(
    modifier: Modifier = Modifier,
    review: ReviewUiState,
    colors: CardColors = MyFarmerTheme.cardColors.primary,
    onDeleteClick: (() -> Unit)? = null,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = colors,
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(
            modifier = Modifier.padding(MyFarmerTheme.paddings.small),
            verticalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.small),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                UserPreviewCard(
                    modifier = Modifier.weight(1f),
                    user = review.author,
                    colors = MyFarmerTheme.cardColors.next(colors),
                )

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    review.createdAt.toLocalDate()?.let {
                        Text(
                            modifier = Modifier.padding(end = 4.dp),
                            text = it,
                            style = MyFarmerTheme.typography.textSmall
                        )
                    }
                    RatingStars(rating = review.rating)
                }
            }

            if (!review.comment.isNullOrBlank()) SelectionContainer {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = review.comment,
                    style = MyFarmerTheme.typography.textMedium
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (onDeleteClick != null)
                    DeleteButton(onClick = onDeleteClick)
            }
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
    MyFarmerPreview {
        ReviewCard(
            review = ReviewUiState(
                id = ReviewId.newId(),
                author = SystemUser(
                    id = UserId.newId(),
                    name = "John Doe super super super super super super super super super super super long name",
                    authId = AuthId(""),
                    profilePicture = ImageResource.EMPTY,
                    contactInfo = ContactInfo.EMPTY
                ),
                rating = Rating(4),
                comment = "Great service and friendly staff! Super super super super super super super super super super super super super super super super super super super super super super super super super super super super super super super super super super super super super super super super super super super super super super super long comment.",
                createdAt = Instant.now(),
            ),
            onDeleteClick = {},
        )
    }
}