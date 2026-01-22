package com.tondracek.myfarmer.ui.createshopflow.common.screen

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.shop.domain.model.ShopInput
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import com.tondracek.myfarmer.ui.common.category.CategoriesRow
import com.tondracek.myfarmer.ui.common.image.ImageView
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.shopdetailscreen.components.sections.MenuSection
import com.tondracek.myfarmer.ui.shopdetailscreen.components.sections.OpeningHoursSection

@Composable
fun ShopFlowReviewAndSubmitStep(
    shopInput: ShopInput,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Header(shopName = shopInput.name)

        HorizontalDivider(modifier = Modifier.padding(MyFarmerTheme.paddings.medium))

        CategoriesRowTitle(categories = shopInput.categories)

        Description(description = shopInput.description)

        ImagesRow(images = shopInput.images)

        MenuSection(
            modifier = Modifier.padding(vertical = MyFarmerTheme.paddings.small),
            menu = shopInput.menu
        )

        OpeningHoursSection(
            modifier = Modifier.padding(vertical = MyFarmerTheme.paddings.small),
            openingHours = shopInput.openingHours,
        )
    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier,
    shopName: String,
) {
    if (shopName.isNotBlank())
        Text(
            modifier = modifier
                .padding(vertical = MyFarmerTheme.paddings.large)
                .fillMaxWidth(),
            text = shopName,
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
