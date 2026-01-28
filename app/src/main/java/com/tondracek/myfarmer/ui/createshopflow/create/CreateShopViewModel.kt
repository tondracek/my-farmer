package com.tondracek.myfarmer.ui.createshopflow.create

import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.core.domain.domainerror.DomainError
import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import com.tondracek.myfarmer.shop.domain.model.ShopInput
import com.tondracek.myfarmer.shop.domain.usecase.CreateShopUC
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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateShopViewModel @Inject constructor(
    private val createShop: CreateShopUC,
) : BaseViewModel<CreateShopEffect>() {

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

    private fun submitShop() = viewModelScope.launch {
        _isLoading.update { true }
        val shopInput: ShopInput = _input.value

        when (val result = createShop(shopInput)) {
            is DomainResult.Success -> {
                emitEffect(CreateShopEffect.ShowCreatedSuccessfully)
                emitEffect(CreateShopEffect.ExitShopCreation)
            }

            is DomainResult.Failure ->
                emitEffect(CreateShopEffect.ShowError(result.error))
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
            emitEffect(CreateShopEffect.RequestExitShopCreationConfirmation)
        }
    }
}

sealed interface CreateShopEffect {

    data class ShowError(val error: DomainError) : CreateShopEffect

    data object ShowCreatedSuccessfully : CreateShopEffect

    data object ExitShopCreation : CreateShopEffect

    data object RequestExitShopCreationConfirmation : CreateShopEffect
}