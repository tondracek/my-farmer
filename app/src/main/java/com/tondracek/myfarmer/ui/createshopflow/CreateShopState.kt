package com.tondracek.myfarmer.ui.createshopflow

import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.shop.domain.model.ShopInput

sealed interface CreateShopState {

    data class Creating(val shopInput: ShopInput, val step: CreateShopStateCreatingStep) :
        CreateShopState {

        enum class CreateShopStateCreatingStep {
            NAME_LOCATION,
            CATEGORIES_MENU,
            PHOTOS_DESCRIPTION,
            OPENING_HOURS,
            REVIEW_AND_SUBMIT
        }

        fun next(): CreateShopState = when (this.step) {
            CreateShopStateCreatingStep.NAME_LOCATION -> this.copy(step = CreateShopStateCreatingStep.CATEGORIES_MENU)
            CreateShopStateCreatingStep.CATEGORIES_MENU -> this.copy(step = CreateShopStateCreatingStep.PHOTOS_DESCRIPTION)
            CreateShopStateCreatingStep.PHOTOS_DESCRIPTION -> this.copy(step = CreateShopStateCreatingStep.OPENING_HOURS)
            CreateShopStateCreatingStep.OPENING_HOURS -> this.copy(step = CreateShopStateCreatingStep.REVIEW_AND_SUBMIT)
            CreateShopStateCreatingStep.REVIEW_AND_SUBMIT -> Finished
        }

        fun previous(): Creating = when (this.step) {
            CreateShopStateCreatingStep.NAME_LOCATION -> this
            CreateShopStateCreatingStep.CATEGORIES_MENU -> this.copy(step = CreateShopStateCreatingStep.NAME_LOCATION)
            CreateShopStateCreatingStep.PHOTOS_DESCRIPTION -> this.copy(step = CreateShopStateCreatingStep.CATEGORIES_MENU)
            CreateShopStateCreatingStep.OPENING_HOURS -> this.copy(step = CreateShopStateCreatingStep.PHOTOS_DESCRIPTION)
            CreateShopStateCreatingStep.REVIEW_AND_SUBMIT -> this.copy(step = CreateShopStateCreatingStep.OPENING_HOURS)
        }

        companion object {
            fun initial() =
                Creating(shopInput = ShopInput(), step = CreateShopStateCreatingStep.NAME_LOCATION)
        }
    }

    data object Finished : CreateShopState

    data object Loading : CreateShopState

    data class Error(val error: UCResult.Failure) : CreateShopState
}