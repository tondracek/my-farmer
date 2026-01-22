package com.tondracek.myfarmer.ui.createshopflow


import com.tondracek.myfarmer.core.domain.domainerror.DomainError

sealed interface CreateUpdateShopFlowEffect {

    data class ShowError(val error: DomainError) : CreateUpdateShopFlowEffect
}