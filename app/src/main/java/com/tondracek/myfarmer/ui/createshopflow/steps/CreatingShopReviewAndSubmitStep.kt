package com.tondracek.myfarmer.ui.createshopflow.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.shop.data.sampleReviewsUI
import com.tondracek.myfarmer.shop.data.shop0
import com.tondracek.myfarmer.shop.domain.model.ShopInput
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import com.tondracek.myfarmer.systemuser.data.sampleUsers
import com.tondracek.myfarmer.ui.common.category.CategoriesRow
import com.tondracek.myfarmer.ui.common.image.ImageView
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.preview.PreviewDark
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.createshopflow.components.NavigationButtons
import com.tondracek.myfarmer.ui.shopdetailscreen.components.ShopDetailLayout
import com.tondracek.myfarmer.ui.shopdetailscreen.components.sections.MenuSection
import com.tondracek.myfarmer.ui.shopdetailscreen.components.sections.OpeningHoursSection
import com.tondracek.myfarmer.ui.shopdetailscreen.toShopDetailState


@Composable
fun CreatingShopReviewAndSubmitStep(
    modifier: Modifier = Modifier,
    state: ShopInput,
    onSubmitCreating: () -> Unit,
    onPreviousStep: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
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

            OpeningHoursSection(
                modifier = Modifier.padding(vertical = MyFarmerTheme.paddings.small),
                openingHours = state.openingHours,
            )
        }

        NavigationButtons(
            modifier = Modifier
                .align(androidx.compose.ui.Alignment.BottomCenter)
                .padding(MyFarmerTheme.paddings.bottomButtons),
            onNext = onSubmitCreating,
            onPrevious = onPreviousStep,
            submitMode = true,
        )
    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier,
    state: ShopInput,
) {
    if (state.name.isNotBlank())
        Text(
            modifier = modifier
                .padding(vertical = MyFarmerTheme.paddings.large)
                .fillMaxWidth(),
            text = state.name,
            style = MyFarmerTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
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
            onReviewsClick = {}
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
            onReviewsClick = {}
        )
    }
}
