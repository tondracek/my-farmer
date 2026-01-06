package com.tondracek.myfarmer.ui.createshopflow.flowcreate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.openinghours.domain.model.OpeningHours
import com.tondracek.myfarmer.productmenu.domain.model.ProductMenu
import com.tondracek.myfarmer.shop.domain.model.ShopInput
import com.tondracek.myfarmer.shop.domain.usecase.CreateShopUC
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import com.tondracek.myfarmer.shoplocation.domain.model.ShopLocation
import com.tondracek.myfarmer.ui.createshopflow.CreateShopState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class CreateShopViewModel @Inject constructor(
    private val createShop: CreateShopUC,
) : ViewModel() {

    private val _state = MutableStateFlow<CreateShopState>(
        CreateShopState.Creating.initial(shopInput = ShopInput())
    )

    val state: StateFlow<CreateShopState> = _state

    private val _effects = MutableSharedFlow<CreateShopEffect>(extraBufferCapacity = 1)
    val effects: SharedFlow<CreateShopEffect> = _effects

    fun goToNextStep() = _state.updateCreating { currentStep ->
        currentStep.next()
    }

    fun goToPreviousStep() = _state.updateCreating { currentStep ->
        currentStep.previous()
    }

    fun navigateBack() = viewModelScope.launch {
        _effects.emit(CreateShopEffect.NavigateBack)
    }

    fun submitCreating() = viewModelScope.launch {
        val currentState = _state.value
        if (currentState !is CreateShopState.Creating) return@launch

        val shopInput = currentState.shopInput
        _state.update { CreateShopState.Loading }

        val result = createShop(shopInput)
        _state.update {
            when (result) {
                is UCResult.Success -> CreateShopState.Finished
                is UCResult.Failure -> CreateShopState.Error(result)
            }
        }
        delay(4.seconds)
        navigateBack()
    }

    /* UPDATE METHODS */
    fun updateName(name: String) = _state.updateShopInput {
        it.copy(name = name)
    }

    fun updateDescription(description: String) = _state.updateShopInput {
        it.copy(description = description)
    }

    fun onOpenAddCategoryDialog() = viewModelScope.launch {
        _effects.emit(CreateShopEffect.OpenAddCategoryDialog)
    }

    fun addCategory(category: ShopCategory) = _state.updateShopInput { shopInput ->
        val newCategories = (shopInput.categories + category).distinctBy(ShopCategory::name)
        shopInput.copy(categories = newCategories)
    }

    fun updateCategories(categories: List<ShopCategory>) = _state.updateShopInput {
        it.copy(categories = categories.distinctBy(ShopCategory::name))
    }

    fun updateImages(images: List<ImageResource>) = _state.updateShopInput {
        it.copy(images = images)
    }

    fun updateMenu(menu: ProductMenu) = _state.updateShopInput {
        it.copy(menu = menu)
    }

    fun updateLocation(location: ShopLocation) = _state.updateShopInput {
        it.copy(location = location)
    }

    fun updateOpeningHours(openingHours: OpeningHours) = _state.updateShopInput {
        it.copy(openingHours = openingHours)
    }

    /* PRIVATE HELPERS */

    fun MutableStateFlow<CreateShopState>.updateCreating(
        update: (CreateShopState.Creating) -> CreateShopState
    ) = this.update {
        when (it) {
            is CreateShopState.Creating -> update(it)
            else -> it
        }
    }

    fun MutableStateFlow<CreateShopState>.updateShopInput(
        update: (ShopInput) -> ShopInput
    ) = this.update {
        when (it) {
            is CreateShopState.Creating -> it.copy(shopInput = update(it.shopInput))
            else -> it
        }
    }
}

sealed interface CreateShopEffect {

    data object NavigateBack : CreateShopEffect

    data object OpenAddCategoryDialog : CreateShopEffect
}