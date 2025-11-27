package com.tondracek.myfarmer.ui.createshopflow

import androidx.compose.runtime.Composable
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.openinghours.domain.model.OpeningHours
import com.tondracek.myfarmer.productmenu.domain.model.ProductMenu
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import com.tondracek.myfarmer.shoplocation.domain.model.ShopLocation
import com.tondracek.myfarmer.ui.common.layout.ErrorLayout
import com.tondracek.myfarmer.ui.common.layout.LoadingLayout

@Composable
fun CreateShopFlowScreen(
    state: CreateShopState,
    onNextStep: () -> Unit,
    onPreviousStep: () -> Unit,
    onUpdateName: (String) -> Unit,
    onUpdateDescription: (String) -> Unit,
    onUpdateCategories: (List<ShopCategory>) -> Unit,
    onUpdateImages: (List<ImageResource>) -> Unit,
    onUpdateLocation: (ShopLocation) -> Unit,
    onUpdateOpeningHours: (OpeningHours) -> Unit,
    onUpdateMenu: (ProductMenu) -> Unit,
    onSubmitCreating: suspend () -> Unit,
    onNavigateBack: () -> Unit,
) {
    when (state) {
        is CreateShopState.Creating -> when (state.step) {
            CreateShopState.Creating.CreateShopStateCreatingStep.NAME_LOCATION -> CreatingShopNameLocationStep(
                state = state,
                onUpdateName = onUpdateName,
                onUpdateLocation = onUpdateLocation,
                onNextStep = onNextStep,
                onPreviousStep = onPreviousStep,
            )

            CreateShopState.Creating.CreateShopStateCreatingStep.CATEGORIES_MENU -> CreatingShopCategoriesMenuStep(
                state = state,
                onUpdateCategories = onUpdateCategories,
                onUpdateMenu = onUpdateMenu,
                onNextStep = onNextStep,
                onPreviousStep = onPreviousStep,
            )

            CreateShopState.Creating.CreateShopStateCreatingStep.PHOTOS_DESCRIPTION -> CreatingShopPhotosDescriptionStep(
                state = state,
                onUpdateDescription = onUpdateDescription,
                onUpdateImages = onUpdateImages,
                onNextStep = onNextStep,
                onPreviousStep = onPreviousStep,
            )

            CreateShopState.Creating.CreateShopStateCreatingStep.OPENING_HOURS -> CreatingShopOpeningHoursStep(
                state = state,
                onUpdateOpeningHours = onUpdateOpeningHours,
                onNextStep = onNextStep,
                onPreviousStep = onPreviousStep,
            )

            CreateShopState.Creating.CreateShopStateCreatingStep.REVIEW_AND_SUBMIT -> CreatingShopReviewAndSubmitStep(
                state = state,
                onSubmitCreating = onSubmitCreating,
                onPreviousStep = onPreviousStep,
            )
        }

        is CreateShopState.Error -> ErrorLayout(
            failure = state.failure,
            onNavigateBack = onNavigateBack
        )

        CreateShopState.Finished -> CraetingShopFinishedScreen()
        CreateShopState.Loading -> LoadingLayout()
    }
}
