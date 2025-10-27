package com.tondracek.myfarmer.ui.common.layout.shopdetaillayout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.tondracek.myfarmer.common.model.ImageResource
import com.tondracek.myfarmer.shop.data.shop1
import com.tondracek.myfarmer.shop.data.shop1reviews
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import com.tondracek.myfarmer.systemuser.data.sampleUsers
import com.tondracek.myfarmer.ui.common.category.CategoriesRow
import com.tondracek.myfarmer.ui.common.image.ImageView
import com.tondracek.myfarmer.ui.core.theme.MyFarmerTheme

@Composable
fun ShopDetailLayout(
    modifier: Modifier = Modifier,
    state: ShopDetailState.Success,
    onReviewsClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        ShopName(state.name)

        CategoriesRowTitle(
            modifier = Modifier.fillMaxWidth(),
            categories = state.categories
        )

        ImagesRow(
            modifier = Modifier
                .fillMaxWidth(),
            images = state.images
        )

        Description(
            modifier = Modifier
                .fillMaxWidth(),
            description = state.description
        )
    }
}


@Composable
private fun ShopName(shopName: String?) {
    if (shopName.isNullOrBlank()) return

    Column {
        Text(
            modifier = Modifier
                .padding(4.dp, 8.dp)
                .fillMaxWidth(),
            text = shopName,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )

        HorizontalDivider()
    }
}

@Composable
private fun ImagesRow(
    modifier: Modifier = Modifier,
    images: List<ImageResource>
) {
    if (images.isEmpty()) return

    Column(
        modifier = modifier,
    ) {
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
fun CategoriesRowTitle(modifier: Modifier = Modifier, categories: List<ShopCategory>) {
    Column(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier.padding(0.dp, 4.dp),
            text = stringResource(R.string.categories),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
        )
        CategoriesRow(categories = categories)
    }
}


@Composable
fun Description(modifier: Modifier = Modifier, description: String?) {
    if (description.isNullOrBlank()) return

    Column(modifier = modifier) {
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
fun ShopDetailLayoutPrev() {
    val shop = shop1

    val owner = sampleUsers.find { it.id == shop.ownerId }!!
    val state = shop.toShopDetailState(owner = owner, reviewsPreview = shop1reviews)

    MyFarmerTheme {
        ShopDetailLayout(
            state = state,
            onReviewsClick = {}
        )
    }
}
