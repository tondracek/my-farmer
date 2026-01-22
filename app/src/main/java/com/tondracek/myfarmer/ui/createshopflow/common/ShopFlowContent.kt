package com.tondracek.myfarmer.ui.createshopflow.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.tondracek.myfarmer.ui.common.layout.LoadingLayout
import com.tondracek.myfarmer.ui.createshopflow.common.components.addcategorydialog.AddCategoryDialogComponent
import com.tondracek.myfarmer.ui.createshopflow.common.components.scaffold.ShopFlowScaffold
import com.tondracek.myfarmer.ui.createshopflow.common.components.scaffold.ShopFlowScaffoldType
import com.tondracek.myfarmer.ui.createshopflow.common.screen.ShopFlowCategoriesMenuStep
import com.tondracek.myfarmer.ui.createshopflow.common.screen.ShopFlowNameLocationStep
import com.tondracek.myfarmer.ui.createshopflow.common.screen.ShopFlowOpeningHoursStep
import com.tondracek.myfarmer.ui.createshopflow.common.screen.ShopFlowPhotosDescriptionStep
import com.tondracek.myfarmer.ui.createshopflow.common.screen.ShopFlowReviewAndSubmitStep

@Composable
fun ShopFlowContent(
    state: ShopFlowState,
    onShopFormEvent: (ShopFormEvent) -> Unit,
    onShopFlowEvent: (ShopFlowEvent) -> Unit,
) {
    var isAddCategoryDialogOpen by remember { mutableStateOf(false) }

    when (state) {
        is ShopFlowState.Editing -> when (state.step) {
            ShopFlowStep.NAME_LOCATION -> ShopFlowScaffold(
                onShopFlowEvent = onShopFlowEvent,
                type = ShopFlowScaffoldType.INITIAL
            ) {
                ShopFlowNameLocationStep(
                    shopInput = state.input,
                    onShopFormEvent = onShopFormEvent,
                )
            }

            ShopFlowStep.CATEGORIES_MENU -> ShopFlowScaffold(onShopFlowEvent = onShopFlowEvent) {
                ShopFlowCategoriesMenuStep(
                    shopInput = state.input,
                    onShopFormEvent = onShopFormEvent,
                    onOpenAddCategoryDialog = { isAddCategoryDialogOpen = true }
                )
            }

            ShopFlowStep.PHOTOS_DESCRIPTION -> ShopFlowScaffold(onShopFlowEvent = onShopFlowEvent) {
                ShopFlowPhotosDescriptionStep(
                    shopInput = state.input,
                    onShopFormEvent = onShopFormEvent,
                )
            }

            ShopFlowStep.OPENING_HOURS -> ShopFlowScaffold(onShopFlowEvent = onShopFlowEvent) {
                ShopFlowOpeningHoursStep(
                    shopInput = state.input,
                    onShopFormEvent = onShopFormEvent,
                )
            }

            ShopFlowStep.REVIEW_AND_SUBMIT -> ShopFlowScaffold(
                onShopFlowEvent = onShopFlowEvent,
                type = ShopFlowScaffoldType.SUBMIT
            ) {
                ShopFlowReviewAndSubmitStep(shopInput = state.input)
            }
        }

        is ShopFlowState.Loading -> LoadingLayout()
    }

    AddCategoryDialogComponent(
        isAddCategoryDialogOpen = isAddCategoryDialogOpen,
        onAddCategoryDialogOpenChange = { isAddCategoryDialogOpen = it },
        onAddCategory = { onShopFormEvent(ShopFormEvent.AddCategory(it)) }
    )
}
