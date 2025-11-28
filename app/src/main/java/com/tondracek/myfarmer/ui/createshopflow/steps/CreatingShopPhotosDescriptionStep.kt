package com.tondracek.myfarmer.ui.createshopflow.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.shop.data.shop0
import com.tondracek.myfarmer.shop.domain.model.ShopInput
import com.tondracek.myfarmer.shop.domain.model.toShopInput
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.createshopflow.components.DescriptionSection
import com.tondracek.myfarmer.ui.createshopflow.components.NavigationButtons
import com.tondracek.myfarmer.ui.createshopflow.components.PhotosSection

@Composable
fun CreatingShopPhotosDescriptionStep(
    shopInput: ShopInput,
    onUpdateDescription: (String) -> Unit,
    onUpdateImages: (List<ImageResource>) -> Unit,
    onNextStep: () -> Unit,
    onPreviousStep: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = MyFarmerTheme.paddings.medium),
            verticalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.medium)
        ) {

            PhotosSection(
                images = shopInput.images,
                onUpdateImages = onUpdateImages,
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = MyFarmerTheme.paddings.xxL),
            )

            DescriptionSection(
                description = shopInput.description,
                onUpdateDescription = onUpdateDescription,
            )
        }

        NavigationButtons(
            modifier = Modifier.padding(MyFarmerTheme.paddings.bottomButtons),
            onNext = onNextStep,
            onPrevious = onPreviousStep,
        )
    }
}


@Preview
@Composable
private fun CreatingShopPhotosDescriptionStepPreview() {
    MyFarmerPreview {
        CreatingShopPhotosDescriptionStep(
            shopInput = shop0.toShopInput(),
            onUpdateDescription = {},
            onUpdateImages = {},
            onNextStep = {},
            onPreviousStep = {}
        )
    }
}