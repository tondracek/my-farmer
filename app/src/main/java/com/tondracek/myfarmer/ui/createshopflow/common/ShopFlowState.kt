package com.tondracek.myfarmer.ui.createshopflow.common

import com.tondracek.myfarmer.shop.domain.model.ShopInput

sealed interface ShopFlowState {

    data class Editing(
        val step: ShopFlowStep,
        val input: ShopInput,
    ) : ShopFlowState

    data object Loading : ShopFlowState

    companion object {
        val Empty: ShopFlowState = Editing(
            step = ShopFlowStep.Initial,
            input = ShopInput.Empty,
        )

        fun initial(input: ShopInput) = Editing(
            step = ShopFlowStep.Initial,
            input = input,
        )
    }
}

enum class ShopFlowStep {
    NAME_LOCATION,
    CATEGORIES_MENU,
    PHOTOS_DESCRIPTION,
    OPENING_HOURS,
    REVIEW_AND_SUBMIT;

    companion object {
        val Initial: ShopFlowStep = NAME_LOCATION
    }

    fun next(): ShopFlowStep = when (this) {
        NAME_LOCATION -> CATEGORIES_MENU
        CATEGORIES_MENU -> PHOTOS_DESCRIPTION
        PHOTOS_DESCRIPTION -> OPENING_HOURS
        OPENING_HOURS -> REVIEW_AND_SUBMIT
        REVIEW_AND_SUBMIT -> REVIEW_AND_SUBMIT
    }

    fun previous(): ShopFlowStep = when (this) {
        NAME_LOCATION -> NAME_LOCATION
        CATEGORIES_MENU -> NAME_LOCATION
        PHOTOS_DESCRIPTION -> CATEGORIES_MENU
        OPENING_HOURS -> PHOTOS_DESCRIPTION
        REVIEW_AND_SUBMIT -> OPENING_HOURS
    }
}

