package com.tondracek.myfarmer.ui.createshopflow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.openinghours.domain.model.OpeningHours
import com.tondracek.myfarmer.productmenu.domain.model.ProductMenu
import com.tondracek.myfarmer.shop.domain.model.ShopInput
import com.tondracek.myfarmer.shop.domain.usecase.CreateShopUC
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import com.tondracek.myfarmer.shoplocation.domain.model.ShopLocation
import com.tondracek.myfarmer.ui.createshopflow.CreateShopFlowStateMachine.next
import com.tondracek.myfarmer.ui.createshopflow.CreateShopFlowStateMachine.previous
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CreateShopViewModel @Inject constructor(
    private val createShop: CreateShopUC
) : ViewModel() {

    private val _step: MutableStateFlow<CreateShopFlowStep> =
        MutableStateFlow(CreateShopFlowStateMachine.initialStep())

    private val _shopInput: MutableStateFlow<ShopInput> = MutableStateFlow(ShopInput())

    val state: StateFlow<CreateShopState> = combine(
        _step,
        _shopInput
    ) { step, shopInput ->
        CreateShopState.Creating(
            step = step,
            shopInput = shopInput
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CreateShopState.Loading
    )

    fun goToNextStep() = _step.update { currentStep ->
        currentStep.next()
    }

    suspend fun finishCreation() {
        createShop(_shopInput.value)
    }

    /* UPDATE METHODS */

    fun goToPreviousStep() = _step.update { currentStep ->
        currentStep.previous()
    }

    fun updateName(name: String) = _shopInput.update {
        it.copy(name = name)
    }

    fun updateDescription(description: String) = _shopInput.update {
        it.copy(description = description)
    }

    fun updateCategories(categories: List<ShopCategory>) = _shopInput.update {
        it.copy(categories = categories)
    }

    fun updateImages(images: List<ImageResource>) = _shopInput.update {
        // TODO: getting the diff between old and new images will be done in updateShopUC, with PhotoStorage
        it.copy(images = images)
    }

    fun updateMenu(menu: ProductMenu) = _shopInput.update {
        it.copy(menu = menu)
    }

    fun updateLocation(location: ShopLocation) = _shopInput.update {
        it.copy(location = location)
    }

    fun updateOpeningHours(openingHours: OpeningHours) = _shopInput.update {
        it.copy(openingHours = openingHours)
    }
}