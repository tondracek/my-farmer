package com.tondracek.myfarmer.ui.createshopflow.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tondracek.myfarmer.productmenu.domain.model.MenuItem
import com.tondracek.myfarmer.shop.data.shop0
import com.tondracek.myfarmer.shop.domain.model.ShopInput
import com.tondracek.myfarmer.shop.domain.model.toShopInput
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.createshopflow.components.NavigationButtons
import com.tondracek.myfarmer.ui.createshopflow.components.category.CategoriesSection
import com.tondracek.myfarmer.ui.createshopflow.components.productmenu.MenuItemsSection

@Composable
fun CreatingShopCategoriesMenuStep(
    shopInput: ShopInput,

    onOpenAddCategoryDialog: () -> Unit,
    onDeleteCategory: (ShopCategory) -> Unit,

    onAddMenuItem: (MenuItem) -> Unit,
    onEditMenuItem: (MenuItem) -> Unit,
    onDeleteMenuItem: (MenuItem) -> Unit,

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
            CategoriesSection(
                categories = shopInput.categories,
                onOpenAddCategoryDialog = onOpenAddCategoryDialog,
                onDeleteCategory = onDeleteCategory,
            )

            MenuItemsSection(
                menu = shopInput.menu,
                onAddMenuItem = onAddMenuItem,
                onEditMenuItem = onEditMenuItem,
                onDeleteMenuItem = onDeleteMenuItem,
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
private fun CreatingShopCategoriesMenuStepPreview() {
    MyFarmerPreview {
        val shopInput = shop0.toShopInput()

        CreatingShopCategoriesMenuStep(
            shopInput = shopInput,

            onOpenAddCategoryDialog = {},
            onDeleteCategory = {},

            onAddMenuItem = {},
            onEditMenuItem = {},
            onDeleteMenuItem = {},

            onNextStep = {},
            onPreviousStep = {}
        )
    }
}