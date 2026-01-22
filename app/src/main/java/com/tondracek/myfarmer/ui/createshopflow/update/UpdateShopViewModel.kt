package com.tondracek.myfarmer.ui.createshopflow.update

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.core.domain.domainerror.DomainError
import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.model.ShopInput
import com.tondracek.myfarmer.shop.domain.model.toShopInput
import com.tondracek.myfarmer.shop.domain.usecase.GetShopByIdUC
import com.tondracek.myfarmer.shop.domain.usecase.UpdateShopUC
import com.tondracek.myfarmer.ui.core.viewmodel.BaseViewModel
import com.tondracek.myfarmer.ui.createshopflow.common.ShopFlowEvent
import com.tondracek.myfarmer.ui.createshopflow.common.ShopFlowState
import com.tondracek.myfarmer.ui.createshopflow.common.ShopFlowStep
import com.tondracek.myfarmer.ui.createshopflow.common.ShopFormEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateShopViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val updateShop: UpdateShopUC,
    private val getShopById: GetShopByIdUC,
) : BaseViewModel<UpdateShopEffect>() {

    private val shopId: ShopId =
        savedStateHandle.getUpdateShopScreenShopId()

    private val _step = MutableStateFlow(ShopFlowStep.Initial)
    private val _input = MutableStateFlow(ShopInput.Empty)

    private val _isLoading = MutableStateFlow(false)

    val state: StateFlow<ShopFlowState> = combine(
        _step,
        _input,
        _isLoading,
    ) { step, input, isLoading ->
        when {
            isLoading -> ShopFlowState.Loading
            else -> ShopFlowState.Editing(step = step, input = input)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ShopFlowState.Loading,
    )

    init {
        viewModelScope.launch {
            getShopById(shopId).first()
                .withFailure {
                    emitEffect(UpdateShopEffect.ShowError(it.error))
                    emitEffect(UpdateShopEffect.ExitShopUpdate)
                }
                .withSuccess { shop -> _input.update { shop.toShopInput() } }
        }
    }

    private fun submitShop() = viewModelScope.launch {
        _isLoading.update { true }
        val shopInput = _input.value

        val result = updateShop(shopId, shopInput)
        when (result) {
            is DomainResult.Success -> {
                emitEffect(UpdateShopEffect.ShowUpdatedSuccessfully)
                emitEffect(UpdateShopEffect.ExitShopUpdate)
            }

            is DomainResult.Failure ->
                emitEffect(UpdateShopEffect.ShowError(result.error))
        }
        _isLoading.update { false }
    }


    fun onShopFormEvent(event: ShopFormEvent) =
        _input.update { ShopFormEvent.applyEvent(it, event) }

    fun onShopFlowEvent(event: ShopFlowEvent) = when (event) {
        is ShopFlowEvent.GoToNextStep -> _step.update { it.next() }
        is ShopFlowEvent.GoToPreviousStep -> _step.update { it.previous() }
        is ShopFlowEvent.Submit -> submitShop()
        ShopFlowEvent.ExitShopFlow -> viewModelScope.launch {
            emitEffect(UpdateShopEffect.RequestExitShopUpdateConfirmation)
        }
    }
}

sealed interface UpdateShopEffect {

    data class ShowError(val error: DomainError) : UpdateShopEffect

    data object ShowUpdatedSuccessfully : UpdateShopEffect

    data object ExitShopUpdate : UpdateShopEffect

    data object RequestExitShopUpdateConfirmation : UpdateShopEffect
}