package com.tondracek.myfarmer.ui.createshopflow


import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.core.domain.domainerror.DomainError
import com.tondracek.myfarmer.location.model.Location
import com.tondracek.myfarmer.openinghours.domain.model.OpeningHours
import com.tondracek.myfarmer.productmenu.domain.model.ProductMenu
import com.tondracek.myfarmer.shop.domain.model.ShopInput
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import com.tondracek.myfarmer.ui.core.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val NEW_CATEGORY_DIALOG_VALUE = "new_category_dialog_value"

abstract class CreateUpdateShopFlowViewmodel() : BaseViewModel<CreateUpdateShopFlowEffect>() {

    protected abstract val mutableState: MutableStateFlow<CreateUpdateShopFlowState>

    val state: StateFlow<CreateUpdateShopFlowState> = mutableState

    fun goToNextStep() = mutableState.updateCreating { currentStep ->
        currentStep.next()
    }

    fun goToPreviousStep() = mutableState.updateCreating { currentStep ->
        currentStep.previous()
    }

    /* UPDATE METHODS */
    fun updateName(name: String) = mutableState.updateShopInput {
        it.copy(name = name)
    }

    fun updateDescription(description: String) = mutableState.updateShopInput {
        it.copy(description = description)
    }

    fun onOpenAddCategoryDialog() = viewModelScope.launch {
        emitEffect(CreateUpdateShopFlowEffect.OpenAddCategoryDialog)
    }

    fun addCategory(category: ShopCategory) = mutableState.updateShopInput { shopInput ->
        val newCategories = (shopInput.categories + category).distinctBy(ShopCategory::name)
        shopInput.copy(categories = newCategories)
    }

    fun updateCategories(categories: List<ShopCategory>) = mutableState.updateShopInput {
        it.copy(categories = categories.distinctBy(ShopCategory::name))
    }

    fun updateImages(images: List<ImageResource>) = mutableState.updateShopInput {
        it.copy(images = images)
    }

    fun updateMenu(menu: ProductMenu) = mutableState.updateShopInput {
        it.copy(menu = menu)
    }

    fun updateLocation(location: Location) = mutableState.updateShopInput {
        it.copy(location = location)
    }

    fun updateOpeningHours(openingHours: OpeningHours) = mutableState.updateShopInput {
        it.copy(openingHours = openingHours)
    }

    /* PRIVATE HELPERS */

    fun MutableStateFlow<CreateUpdateShopFlowState>.updateCreating(
        update: (CreateUpdateShopFlowState.Creating) -> CreateUpdateShopFlowState
    ) = this.update {
        when (it) {
            is CreateUpdateShopFlowState.Creating -> update(it)
            else -> it
        }
    }

    fun MutableStateFlow<CreateUpdateShopFlowState>.updateShopInput(
        update: (ShopInput) -> ShopInput
    ) = this.update {
        when (it) {
            is CreateUpdateShopFlowState.Creating -> it.copy(shopInput = update(it.shopInput))
            else -> it
        }
    }
}

sealed interface CreateUpdateShopFlowEffect {

    data object NavigateBack : CreateUpdateShopFlowEffect

    data object OpenAddCategoryDialog : CreateUpdateShopFlowEffect

    data object ShowShopCreatedSuccessfully : CreateUpdateShopFlowEffect

    data object ShowShopUpdatedSuccessfully : CreateUpdateShopFlowEffect

    data class ShowError(val error: DomainError) : CreateUpdateShopFlowEffect
}