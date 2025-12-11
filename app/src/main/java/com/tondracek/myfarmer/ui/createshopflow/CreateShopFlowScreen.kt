package com.tondracek.myfarmer.ui.createshopflow

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.openinghours.domain.model.OpeningHours
import com.tondracek.myfarmer.productmenu.domain.model.ProductMenu
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import com.tondracek.myfarmer.shoplocation.domain.model.ShopLocation
import com.tondracek.myfarmer.ui.common.layout.CardSuccessMessageLayout
import com.tondracek.myfarmer.ui.common.layout.ErrorLayout
import com.tondracek.myfarmer.ui.common.layout.LoadingLayout
import com.tondracek.myfarmer.ui.createshopflow.steps.CreatingShopCategoriesMenuStep
import com.tondracek.myfarmer.ui.createshopflow.steps.CreatingShopNameLocationStep
import com.tondracek.myfarmer.ui.createshopflow.steps.CreatingShopOpeningHoursStep
import com.tondracek.myfarmer.ui.createshopflow.steps.CreatingShopPhotosDescriptionStep
import com.tondracek.myfarmer.ui.createshopflow.steps.CreatingShopReviewAndSubmitStep

enum class CreateShopFlowMode { CREATE, UPDATE }

@Composable
fun CreateShopFlowScreen(
    state: CreateShopState,
    createShopFlowMode: CreateShopFlowMode,
    onNextStep: () -> Unit,
    onPreviousStep: () -> Unit,
    onUpdateName: (String) -> Unit,
    onUpdateDescription: (String) -> Unit,
    onOpenAddCategoryDialog: () -> Unit,
    onUpdateCategories: (List<ShopCategory>) -> Unit,
    onUpdateImages: (List<ImageResource>) -> Unit,
    onUpdateLocation: (ShopLocation) -> Unit,
    onUpdateOpeningHours: (OpeningHours) -> Unit,
    onUpdateMenu: (ProductMenu) -> Unit,
    onSubmitCreating: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    when (state) {
        is CreateShopState.Creating -> when (state.step) {
            CreateShopState.Creating.CreateShopStateCreatingStep.NAME_LOCATION -> CreatingShopNameLocationStep(
                shopInput = state.shopInput,
                onUpdateName = onUpdateName,
                onUpdateLocation = onUpdateLocation,
                onNextStep = onNextStep,
                onPreviousStep = onPreviousStep,
            )

            CreateShopState.Creating.CreateShopStateCreatingStep.CATEGORIES_MENU -> CreatingShopCategoriesMenuStep(
                shopInput = state.shopInput,
                onOpenAddCategoryDialog = onOpenAddCategoryDialog,
                onUpdateCategories = onUpdateCategories,
                onUpdateMenu = onUpdateMenu,
                onNextStep = onNextStep,
                onPreviousStep = onPreviousStep,
            )

            CreateShopState.Creating.CreateShopStateCreatingStep.PHOTOS_DESCRIPTION -> CreatingShopPhotosDescriptionStep(
                shopInput = state.shopInput,
                onUpdateDescription = onUpdateDescription,
                onUpdateImages = onUpdateImages,
                onNextStep = onNextStep,
                onPreviousStep = onPreviousStep,
            )

            CreateShopState.Creating.CreateShopStateCreatingStep.OPENING_HOURS -> CreatingShopOpeningHoursStep(
                shopInput = state.shopInput,
                onUpdateOpeningHours = onUpdateOpeningHours,
                onNextStep = onNextStep,
                onPreviousStep = onPreviousStep,
            )

            CreateShopState.Creating.CreateShopStateCreatingStep.REVIEW_AND_SUBMIT -> CreatingShopReviewAndSubmitStep(
                state = state.shopInput,
                onSubmitCreating = onSubmitCreating,
                onPreviousStep = onPreviousStep,
            )
        }

        is CreateShopState.Error -> ErrorLayout(
            failure = state.failure,
            onNavigateBack = onNavigateBack
        )

        CreateShopState.Finished -> CardSuccessMessageLayout(
            title = when (createShopFlowMode) {
                CreateShopFlowMode.CREATE -> stringResource(R.string.shop_created_successfully)
                CreateShopFlowMode.UPDATE -> stringResource(R.string.shop_updated_successfully)
            }
        )

        CreateShopState.Loading -> LoadingLayout()
    }
}
