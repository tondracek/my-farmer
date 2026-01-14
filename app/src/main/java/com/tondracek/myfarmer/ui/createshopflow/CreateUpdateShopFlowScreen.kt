package com.tondracek.myfarmer.ui.createshopflow

import androidx.compose.runtime.Composable
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.location.model.Location
import com.tondracek.myfarmer.openinghours.domain.model.OpeningHours
import com.tondracek.myfarmer.productmenu.domain.model.ProductMenu
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import com.tondracek.myfarmer.ui.common.layout.LoadingLayout
import com.tondracek.myfarmer.ui.createshopflow.steps.CreatingShopCategoriesMenuStep
import com.tondracek.myfarmer.ui.createshopflow.steps.CreatingShopNameLocationStep
import com.tondracek.myfarmer.ui.createshopflow.steps.CreatingShopOpeningHoursStep
import com.tondracek.myfarmer.ui.createshopflow.steps.CreatingShopPhotosDescriptionStep
import com.tondracek.myfarmer.ui.createshopflow.steps.CreatingShopReviewAndSubmitStep

@Composable
fun CreateUpdateShopFlowScreen(
    state: CreateUpdateShopFlowState,
    onNextStep: () -> Unit,
    onPreviousStep: () -> Unit,
    onUpdateName: (String) -> Unit,
    onUpdateDescription: (String) -> Unit,
    onOpenAddCategoryDialog: () -> Unit,
    onUpdateCategories: (List<ShopCategory>) -> Unit,
    onUpdateImages: (List<ImageResource>) -> Unit,
    onUpdateLocation: (Location) -> Unit,
    onUpdateOpeningHours: (OpeningHours) -> Unit,
    onUpdateMenu: (ProductMenu) -> Unit,
    onSubmitCreating: () -> Unit,
) {
    when (state) {
        is CreateUpdateShopFlowState.Creating -> when (state.step) {
            CreateUpdateShopFlowState.Creating.CreateShopStateCreatingStep.NAME_LOCATION -> CreatingShopNameLocationStep(
                shopInput = state.shopInput,
                onUpdateName = onUpdateName,
                onUpdateLocation = onUpdateLocation,
                onNextStep = onNextStep,
                onPreviousStep = onPreviousStep,
            )

            CreateUpdateShopFlowState.Creating.CreateShopStateCreatingStep.CATEGORIES_MENU -> CreatingShopCategoriesMenuStep(
                shopInput = state.shopInput,
                onOpenAddCategoryDialog = onOpenAddCategoryDialog,
                onUpdateCategories = onUpdateCategories,
                onUpdateMenu = onUpdateMenu,
                onNextStep = onNextStep,
                onPreviousStep = onPreviousStep,
            )

            CreateUpdateShopFlowState.Creating.CreateShopStateCreatingStep.PHOTOS_DESCRIPTION -> CreatingShopPhotosDescriptionStep(
                shopInput = state.shopInput,
                onUpdateDescription = onUpdateDescription,
                onUpdateImages = onUpdateImages,
                onNextStep = onNextStep,
                onPreviousStep = onPreviousStep,
            )

            CreateUpdateShopFlowState.Creating.CreateShopStateCreatingStep.OPENING_HOURS -> CreatingShopOpeningHoursStep(
                shopInput = state.shopInput,
                onUpdateOpeningHours = onUpdateOpeningHours,
                onNextStep = onNextStep,
                onPreviousStep = onPreviousStep,
            )

            CreateUpdateShopFlowState.Creating.CreateShopStateCreatingStep.REVIEW_AND_SUBMIT -> CreatingShopReviewAndSubmitStep(
                state = state.shopInput,
                onSubmitCreating = onSubmitCreating,
                onPreviousStep = onPreviousStep,
            )
        }

        CreateUpdateShopFlowState.Loading -> LoadingLayout()
    }
}
