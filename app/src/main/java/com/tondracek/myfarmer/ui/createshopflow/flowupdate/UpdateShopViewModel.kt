package com.tondracek.myfarmer.ui.createshopflow.flowupdate

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.model.toShopInput
import com.tondracek.myfarmer.shop.domain.usecase.GetShopByIdUC
import com.tondracek.myfarmer.shop.domain.usecase.UpdateShopUC
import com.tondracek.myfarmer.ui.createshopflow.CreateUpdateShopFlowEffect
import com.tondracek.myfarmer.ui.createshopflow.CreateUpdateShopFlowState
import com.tondracek.myfarmer.ui.createshopflow.CreateUpdateShopFlowViewmodel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateShopViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getShopById: GetShopByIdUC,
    private val updateShop: UpdateShopUC,
) : CreateUpdateShopFlowViewmodel() {

    private val shopId: ShopId = savedStateHandle.getUpdateShopScreenShopId()

    init {
        viewModelScope.launch {
            mutableState.update {
                getShopById(shopId)
                    .first()
                    .fold(
                        onSuccess = { CreateUpdateShopFlowState.Creating.initial(it.toShopInput()) },
                        onFailure = { CreateUpdateShopFlowState.Error(it) }
                    )
            }
        }
    }

    fun submitUpdating() = viewModelScope.launch {
        val currentState = mutableState.value
        if (currentState !is CreateUpdateShopFlowState.Creating) return@launch

        val shopInput = currentState.shopInput
        mutableState.update { CreateUpdateShopFlowState.Loading }

        val result = updateShop(shopId = shopId, input = shopInput)
        when (result) {
            is UCResult.Success -> _effects.emit(CreateUpdateShopFlowEffect.ShowShopUpdatedSuccessfully)
            is UCResult.Failure -> _effects.emit(CreateUpdateShopFlowEffect.ShowError(message = result.userError))
        }
        navigateBack()
    }
}
