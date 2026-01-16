package com.tondracek.myfarmer.ui.createshopflow.flowcreate

import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import com.tondracek.myfarmer.shop.domain.usecase.CreateShopUC
import com.tondracek.myfarmer.ui.createshopflow.CreateUpdateShopFlowEffect
import com.tondracek.myfarmer.ui.createshopflow.CreateUpdateShopFlowState
import com.tondracek.myfarmer.ui.createshopflow.CreateUpdateShopFlowViewmodel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateShopViewModel @Inject constructor(
    private val createShop: CreateShopUC,
) : CreateUpdateShopFlowViewmodel() {

    init {
        mutableState.update {
            CreateUpdateShopFlowState.Creating.initial()
        }
    }

    fun submitCreating() = viewModelScope.launch {
        val currentState = mutableState.value
        if (currentState !is CreateUpdateShopFlowState.Creating) return@launch

        val shopInput = currentState.shopInput

        mutableState.update { CreateUpdateShopFlowState.Loading }
        val result = createShop(shopInput)
        when (result) {
            is DomainResult.Success -> {
                emitEffect(CreateUpdateShopFlowEffect.ShowShopCreatedSuccessfully)
                emitEffect(CreateUpdateShopFlowEffect.NavigateBack)
            }

            is DomainResult.Failure -> {
                emitEffect(CreateUpdateShopFlowEffect.ShowError(result.error))
                mutableState.update { currentState }
            }
        }
    }
}