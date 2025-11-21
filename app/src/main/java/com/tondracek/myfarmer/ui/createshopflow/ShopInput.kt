package com.tondracek.myfarmer.ui.createshopflow

import com.tondracek.myfarmer.shop.domain.model.ShopInput

sealed interface CreateShopState {

    data class Creating(
        val step: CreateShopFlowStep,
        val shopInput: ShopInput
    ) : CreateShopState

    data object Loading : CreateShopState

    data object Error : CreateShopState
}