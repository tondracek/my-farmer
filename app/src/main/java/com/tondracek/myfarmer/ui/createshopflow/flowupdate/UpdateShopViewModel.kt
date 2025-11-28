package com.tondracek.myfarmer.ui.createshopflow.flowupdate

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.openinghours.domain.model.OpeningHours
import com.tondracek.myfarmer.productmenu.domain.model.ProductMenu
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.model.ShopInput
import com.tondracek.myfarmer.shop.domain.model.toShopInput
import com.tondracek.myfarmer.shop.domain.usecase.GetShopByIdUC
import com.tondracek.myfarmer.shop.domain.usecase.UpdateShopUC
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import com.tondracek.myfarmer.shoplocation.domain.model.ShopLocation
import com.tondracek.myfarmer.ui.core.navigation.AppNavigator
import com.tondracek.myfarmer.ui.createshopflow.CreateShopState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateShopViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getShopById: GetShopByIdUC,
    private val updateShop: UpdateShopUC,
    private val navigator: AppNavigator,
) : ViewModel() {

    private val shopId: ShopId = savedStateHandle.getUpdateShopScreenShopId()

    private val _state = MutableStateFlow<CreateShopState>(
        CreateShopState.Creating.initial(
            ShopInput()
        )
    )

    init {
        viewModelScope.launch {
            _state.update {
                getShopById(shopId)
                    .first()
                    .fold(
                        onSuccess = { CreateShopState.Creating.initial(it.toShopInput()) },
                        onFailure = { CreateShopState.Error(it) }
                    )
            }
        }
    }

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
        _state.updateCreatingSuspend {
            val result = updateShop(shopId = shopId, input = it.shopInput)
            when (result) {
                is UCResult.Success<Unit> -> CreateShopState.Finished
                is UCResult.Failure -> CreateShopState.Error(result)
            }
        }
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