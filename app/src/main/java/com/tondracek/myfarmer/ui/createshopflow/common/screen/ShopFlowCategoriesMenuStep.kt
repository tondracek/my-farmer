package com.tondracek.myfarmer.ui.createshopflow.common.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tondracek.myfarmer.shop.domain.model.ShopInput
import com.tondracek.myfarmer.shop.domain.model.toShopInput
import com.tondracek.myfarmer.shop.sample.shop0
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.createshopflow.common.ShopFormEvent
import com.tondracek.myfarmer.ui.createshopflow.common.components.category.CategoriesSection
import com.tondracek.myfarmer.ui.createshopflow.common.components.productmenu.MenuItemsSection

@Composable
fun ShopFlowCategoriesMenuStep(
    shopInput: ShopInput,
    onShopFormEvent: (ShopFormEvent) -> Unit,
    onOpenAddCategoryDialog: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = MyFarmerTheme.paddings.medium),
        verticalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.medium)
    ) {
        CategoriesSection(
            categories = shopInput.categories,
            onOpenAddCategoryDialog = onOpenAddCategoryDialog,
            onDeleteCategory = { onShopFormEvent(ShopFormEvent.RemoveCategory(it)) }
        )

        MenuItemsSection(
            menu = shopInput.menu,
            onAddMenuItem = { onShopFormEvent(ShopFormEvent.AddMenuItem(it)) },
            onEditMenuItem = { onShopFormEvent(ShopFormEvent.EditMenuItem(it)) },
            onDeleteMenuItem = { onShopFormEvent(ShopFormEvent.RemoveMenuItem(it)) }
        )
    }
}

@Preview
@Composable
private fun CreatingShopCategoriesMenuStepPreview() {
    MyFarmerPreview {
        val shopInput = shop0.toShopInput()

        ShopFlowCategoriesMenuStep(
            shopInput = shopInput,
            onShopFormEvent = {},
            onOpenAddCategoryDialog = {}
        )
    }
}