package com.tondracek.myfarmer.ui.createshopflow.flowcreate

import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.shop.domain.usecase.CreateShopUC
import com.tondracek.myfarmer.ui.createshopflow.CreateUpdateShopFlowState
import com.tondracek.myfarmer.ui.createshopflow.CreateUpdateShopFlowViewmodel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class CreateShopViewModel @Inject constructor(
    private val createShop: CreateShopUC,
) : CreateUpdateShopFlowViewmodel() {

    fun submitCreating() = viewModelScope.launch {
        val currentState = mutableState.value
        if (currentState !is CreateUpdateShopFlowState.Creating) return@launch

        val shopInput = currentState.shopInput
        mutableState.update { CreateUpdateShopFlowState.Loading }

        val result = createShop(shopInput)
        mutableState.update {
            when (result) {
                is UCResult.Success -> CreateUpdateShopFlowState.Finished
                is UCResult.Failure -> CreateUpdateShopFlowState.Error(result)
            }
        }
        delay(4.seconds)
        navigateBack()
    }
}