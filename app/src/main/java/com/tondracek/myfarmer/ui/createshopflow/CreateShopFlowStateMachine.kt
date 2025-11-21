package com.tondracek.myfarmer.ui.createshopflow

enum class CreateShopFlowStep(val order: Int) {
    NameLocationStep(0),
    CategoriesMenuStep(1),
    PhotosDescriptionStep(2),
    OpeningHoursStep(3),
    ReviewAndSubmitStep(4),
    Finished(5),
}

object CreateShopFlowStateMachine {

    private val allSteps = CreateShopFlowStep.entries.sortedBy { it.order }

    fun initialStep(): CreateShopFlowStep = CreateShopFlowStep.NameLocationStep

    fun CreateShopFlowStep.next(): CreateShopFlowStep {
        val nextIndex = allSteps.indexOf(this) + 1
        return allSteps.getOrNull(nextIndex)
            ?: CreateShopFlowStep.Finished
    }

    fun CreateShopFlowStep.previous(): CreateShopFlowStep {
        val previousIndex = allSteps.indexOf(this) - 1
        return allSteps.getOrNull(previousIndex)
            ?: CreateShopFlowStep.NameLocationStep
    }
}