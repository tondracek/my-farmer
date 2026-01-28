package com.tondracek.myfarmer.ui.shopdetailscreen.components

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.image.model.ImageResource
import com.tondracek.myfarmer.location.domain.model.Location
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import com.tondracek.myfarmer.ui.common.category.CategoriesRow
import com.tondracek.myfarmer.ui.common.image.ImageView
import com.tondracek.myfarmer.ui.common.rating.RatingStars
import com.tondracek.myfarmer.ui.common.sample.sampleReviewsUI
import com.tondracek.myfarmer.ui.common.sample.sampleUsers
import com.tondracek.myfarmer.ui.common.sample.shop0
import com.tondracek.myfarmer.ui.common.user.UserPreviewCard
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.preview.PreviewDark
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.shopdetailscreen.ShopDetailState
import com.tondracek.myfarmer.ui.shopdetailscreen.components.sections.ContactInfoSection
import com.tondracek.myfarmer.ui.shopdetailscreen.components.sections.MenuSection
import com.tondracek.myfarmer.ui.shopdetailscreen.components.sections.OpeningHoursSection
import com.tondracek.myfarmer.ui.shopdetailscreen.components.sections.ReviewsPreviewSection
import com.tondracek.myfarmer.ui.shopdetailscreen.toShopDetailState

@Composable
fun ShopDetailLayout(
    modifier: Modifier = Modifier,
    state: ShopDetailState.Success,
    onReviewsClick: () -> Unit,
    showErrorMessage: (String) -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Header(state = state)

        HorizontalDivider(modifier = Modifier.padding(MyFarmerTheme.paddings.medium))

        CategoriesRowTitle(categories = state.categories)

        Description(description = state.description)

        ImagesRow(images = state.images)

        MenuSection(
            modifier = Modifier.padding(vertical = MyFarmerTheme.paddings.small),
            menu = state.menu
        )

        if (!state.owner.contactInfo.isEmpty())
            ContactInfoSection(
                contactInfo = state.owner.contactInfo,
                showErrorMessage = showErrorMessage,
            )

        OpeningHoursSection(
            modifier = Modifier.padding(vertical = MyFarmerTheme.paddings.small),
            openingHours = state.openingHours,
        )

        ReviewsPreviewSection(
            modifier = Modifier.padding(vertical = MyFarmerTheme.paddings.small),
            reviews = state.reviewsPreview,
            onReviewsClick = onReviewsClick,
        )
    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier,
    state: ShopDetailState.Success,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        NavigateButton(
            modifier = Modifier.align(Alignment.TopEnd),
            location = state.location
        )
        Column(Modifier.align(Alignment.Center)) {
            Text(
                modifier = Modifier
                    .padding(vertical = MyFarmerTheme.paddings.large)
                    .fillMaxWidth(),
                text = state.name,
                style = MyFarmerTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    UserPreviewCard(user = state.owner)
                }

                RatingStars(rating = state.averageRating)
            }
        }
    }
}

@Composable
private fun NavigateButton(
    modifier: Modifier = Modifier,
    location: Location
) {
    val context = LocalContext.current

    IconButton(
        modifier = modifier,
        onClick = {
            val uri =
                "geo:${location.latitude},${location.longitude}?q=${location.latitude},${location.longitude}".toUri()
            val mapIntent = Intent(Intent.ACTION_VIEW, uri).apply {
                setPackage("com.google.android.apps.maps")
            }
            try {
                context.startActivity(mapIntent)
            } catch (_: ActivityNotFoundException) {
                val browserUri =
                    "https://www.google.com/maps/search/?api=1&query=${location.latitude},${location.longitude}".toUri()
                context.startActivity(Intent(Intent.ACTION_VIEW, browserUri))
            }
        },
        colors = MyFarmerTheme.iconButtonColors.primary
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Navigate to shop",
        )
    }
}

@Composable
private fun ImagesRow(
    modifier: Modifier = Modifier,
    images: List<ImageResource>
) {
    if (images.isEmpty()) return

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(0.dp, 4.dp),
            text = stringResource(R.string.gallery),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
        )
        LazyRow(
            modifier = Modifier.padding(4.dp, 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            items(images) {
                ImageView(
                    modifier = Modifier.height(100.dp),
                    imageResource = it,
                )
            }
        }
    }
}

@Composable
private fun CategoriesRowTitle(modifier: Modifier = Modifier, categories: List<ShopCategory>) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = stringResource(R.string.categories),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
        )
        CategoriesRow(categories = categories)
    }
}


@Composable
private fun Description(modifier: Modifier = Modifier, description: String?) {
    if (description.isNullOrBlank()) return

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = stringResource(R.string.description),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
        )
        Text(text = description)
    }
}

@Preview
@Composable
private fun ShopDetailLayoutPrev() {
    val shop = shop0

    val owner = sampleUsers.find { it.id == shop.ownerId }!!
    val reviews = sampleReviewsUI.take(3)
    val state = shop.toShopDetailState(
        owner = owner,
        reviewsPreview = reviews,
        averageRating = Rating(3)
    )

    MyFarmerPreview {
        ShopDetailLayout(
            state = state,
            onReviewsClick = {},
            showErrorMessage = {},
        )
    }
}


@PreviewDark
@Composable
private fun ShopDetailLayoutPrev0() {
    val shop = shop0

    val owner = sampleUsers.find { it.id == shop.ownerId }!!
    val reviews = sampleReviewsUI.take(3)
    val state = shop.toShopDetailState(
        owner = owner,
        reviewsPreview = reviews,
        averageRating = Rating(3)
    )

    MyFarmerPreview {
        ShopDetailLayout(
            state = state,
            onReviewsClick = {},
            showErrorMessage = {},
        )
    }
}
