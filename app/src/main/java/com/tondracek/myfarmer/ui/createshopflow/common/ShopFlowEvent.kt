package com.tondracek.myfarmer.ui.createshopflow.common

sealed interface ShopFlowEvent {

    data object GoToPreviousStep : ShopFlowEvent

    data object GoToNextStep : ShopFlowEvent

    data object Submit : ShopFlowEvent

    data object ExitShopFlow : ShopFlowEvent
}
