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
import com.tondracek.myfarmer.ui.core.navigation.AppNavigator
import com.tondracek.myfarmer.ui.createshopflow.CreateShopState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class CreateShopViewModel @Inject constructor(
    private val createShop: CreateShopUC,
    private val navigator: AppNavigator,
) : ViewModel() {

    private val _state = MutableStateFlow<CreateShopState>(CreateShopState.Creating.initial())

    val state: StateFlow<CreateShopState> = _state

    fun goToNextStep() = _state.updateCreating { currentStep ->
        currentStep.next()
    }

    fun goToPreviousStep() = _state.updateCreating { currentStep ->
        currentStep.previous()
    }

    fun navigateBack() =
        navigator.navigateBack()

    fun submitCreating() = viewModelScope.launch {
        val currentState = _state.value
        if (currentState !is CreateShopState.Creating) return@launch

        val shopInput = currentState.shopInput
        _state.update { CreateShopState.Loading }

        val result = createShop(shopInput)
        _state.update {
            when (result) {
                is UCResult.Success -> {
                    CreateShopState.Finished
                }

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

    fun updateCategories(categories: List<ShopCategory>) = _state.updateShopInput {
        it.copy(categories = categories)
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

    suspend fun MutableStateFlow<CreateShopState>.updateCreatingSuspend(
        update: suspend (CreateShopState.Creating) -> CreateShopState
    ) = this.update {
        when (it) {
            is CreateShopState.Creating -> update(it)
            else -> it
        }
    }

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