package com.tondracek.myfarmer.ui.createshopflow.common.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tondracek.myfarmer.shop.data.shop0
import com.tondracek.myfarmer.shop.domain.model.ShopInput
import com.tondracek.myfarmer.shop.domain.model.toShopInput
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.createshopflow.common.ShopFormEvent
import com.tondracek.myfarmer.ui.createshopflow.common.components.DescriptionSection
import com.tondracek.myfarmer.ui.createshopflow.common.components.PhotosSection

@Composable
fun ShopFlowPhotosDescriptionStep(
    shopInput: ShopInput,
    onShopFormEvent: (ShopFormEvent) -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = MyFarmerTheme.paddings.medium),
        verticalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.medium)
    ) {

        PhotosSection(
            images = shopInput.images,
            onAddImage = { onShopFormEvent(ShopFormEvent.AddImage(it)) },
            onMoveImageLeft = { onShopFormEvent(ShopFormEvent.MoveImageLeft(it)) },
            onMoveImageRight = { onShopFormEvent(ShopFormEvent.MoveImageRight(it)) },
            onRemoveImage = { onShopFormEvent(ShopFormEvent.RemoveImage(it)) },
        )

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = MyFarmerTheme.paddings.xxL),
        )

        DescriptionSection(
            description = shopInput.description,
            onUpdateDescription = { onShopFormEvent(ShopFormEvent.UpdateDescription(it)) }
        )
    }
}


@Preview
@Composable
private fun CreatingShopPhotosDescriptionStepPreview() {
    MyFarmerPreview {
        ShopFlowPhotosDescriptionStep(
            shopInput = shop0.toShopInput(),
            onShopFormEvent = {},
        )
    }
}