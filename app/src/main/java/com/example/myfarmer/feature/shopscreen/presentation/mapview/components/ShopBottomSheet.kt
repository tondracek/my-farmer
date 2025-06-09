package com.example.myfarmer.feature.shopscreen.presentation.mapview.components

import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfarmer.R
import com.example.myfarmer.shared.domain.Category
import com.example.myfarmer.shared.domain.ImageResource
import com.example.myfarmer.shared.domain.Shop
import com.example.myfarmer.shared.domain.sampleShops
import com.example.myfarmer.shared.theme.MyFarmerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopBottomSheet(shop: Shop, onDismissRequest: () -> Unit = {}) {
    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .padding(4.dp, 8.dp)
                    .fillMaxWidth(),
                text = shop.name,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )

            HorizontalDivider()

            CategoriesRowTitle(
                modifier = Modifier.fillMaxWidth(),
                categories = shop.categories
            )

            ImagesRow(
                modifier = Modifier
                    .fillMaxWidth(),
                images = shop.images
            )

            Description(
                modifier = Modifier
                    .fillMaxWidth(),
                description = shop.description
            )
        }
    }
}

@Composable
fun ImagesRow(
    modifier: Modifier = Modifier,
    images: List<ImageResource>
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier.padding(0.dp, 4.dp),
            text = stringResource(R.string.gallery),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
        )
        if (images.isEmpty()) {
            Text(
                text = stringResource(R.string.no_pictures_found),
                modifier = Modifier.padding(4.dp, 4.dp),
                fontStyle = FontStyle.Italic,
            )
        } else {
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
}

@Composable
fun CategoriesRowTitle(modifier: Modifier = Modifier, categories: List<Category>) {
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
fun CategoriesRow(categories: List<Category>) {
    Row(
        modifier = Modifier
            .padding(0.dp, 4.dp)
            .horizontalScroll(rememberScrollState()),
    ) {
        categories.forEach { category ->
            Box(
                modifier = Modifier.padding(4.dp, 0.dp)
            ) {
                CategoryLabel(category = category)
            }
        }
    }
}

@Composable
fun Description(modifier: Modifier = Modifier, description: String) {
    Column(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier.padding(0.dp, 4.dp),
            text = stringResource(R.string.description),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
        )
        Text(
            text = description,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ShopBottomSheetPrev() {
    MyFarmerTheme {
        ShopBottomSheet(
            shop = sampleShops.first()
        )
    }
}