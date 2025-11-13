package com.tondracek.myfarmer.ui.common.layout.shopdetaillayout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.common.model.ImageResource
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.shop.data.sampleReviews
import com.tondracek.myfarmer.shop.data.shop1
import com.tondracek.myfarmer.shop.data.shop2
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import com.tondracek.myfarmer.systemuser.data.sampleUsers
import com.tondracek.myfarmer.ui.common.category.CategoriesRow
import com.tondracek.myfarmer.ui.common.image.ImageView
import com.tondracek.myfarmer.ui.common.layout.shopdetaillayout.components.MenuSection
import com.tondracek.myfarmer.ui.common.rating.RatingStars
import com.tondracek.myfarmer.ui.common.user.UserPreviewCard
import com.tondracek.myfarmer.ui.core.preview.AsyncImagePreviewFix
import com.tondracek.myfarmer.ui.core.preview.PreviewDark
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun ShopDetailLayout(
    modifier: Modifier = Modifier,
    state: ShopDetailState.Success,
    onReviewsClick: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {
        Header(state)

        CategoriesRowTitle(categories = state.categories)

        Description(description = state.description)

        ImagesRow(images = state.images)

        Spacer(modifier = Modifier.padding(vertical = MyFarmerTheme.paddings.large))

        MenuSection(menu = state.menu)

//        ContactInfoSection( TODO
//            state.owner.contactInfo
//        )
    }
}


@Composable
private fun Header(
    state: ShopDetailState.Success,
) {
    Column(Modifier.fillMaxWidth()) {
        if (!state.name.isNullOrBlank())
            Text(
                modifier = Modifier
                    .padding(vertical = MyFarmerTheme.paddings.large)
                    .fillMaxWidth(),
                text = state.name,
                style = MyFarmerTheme.typography.headerMedium,
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

        HorizontalDivider(modifier = Modifier.padding(MyFarmerTheme.paddings.medium))
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

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(0.dp, 4.dp),
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
    val shop = shop1

    val owner = sampleUsers.find { it.id == shop.ownerId }!!
    val reviews = sampleReviews.filter { it.shopId == shop.id }
    val state = shop.toShopDetailState(
        owner = owner,
        reviewsPreview = reviews,
        averageRating = Rating(3)
    )

    MyFarmerTheme {
        AsyncImagePreviewFix {
            ShopDetailLayout(
                state = state,
                onReviewsClick = {}
            )
        }
    }
}


@PreviewDark
@Composable
private fun ShopDetailLayoutPrev0() {
    val shop = shop2

    val owner = sampleUsers.find { it.id == shop.ownerId }!!
    val reviews = sampleReviews.filter { it.shopId == shop.id }
    val state = shop.toShopDetailState(
        owner = owner,
        reviewsPreview = reviews,
        averageRating = Rating(3)
    )

    MyFarmerTheme {
        AsyncImagePreviewFix {
            ShopDetailLayout(
                state = state,
                onReviewsClick = {}
            )
        }
    }
}
