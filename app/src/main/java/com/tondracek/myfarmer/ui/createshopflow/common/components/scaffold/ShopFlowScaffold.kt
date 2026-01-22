package com.tondracek.myfarmer.ui.createshopflow.common.components.scaffold

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.createshopflow.common.ShopFlowEvent

enum class ShopFlowScaffoldType {
    INITIAL,
    STANDARD,
    SUBMIT,
}

@Composable
fun ShopFlowScaffold(
    onShopFlowEvent: (ShopFlowEvent) -> Unit,
    type: ShopFlowScaffoldType = ShopFlowScaffoldType.STANDARD,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(modifier = Modifier.weight(1f)) { content() }

        ShopFlowScaffoldNavigationButtons(
            type = type,
            onShopFlowEvent = onShopFlowEvent,
        )
    }
}

@Composable
private fun ShopFlowScaffoldNavigationButtons(
    type: ShopFlowScaffoldType,
    onShopFlowEvent: (ShopFlowEvent) -> Unit,
) {
    when (type) {
        ShopFlowScaffoldType.INITIAL -> NavigationButtons(
            modifier = Modifier.padding(MyFarmerTheme.paddings.bottomButtons),
            onNext = { onShopFlowEvent(ShopFlowEvent.GoToNextStep) },
            onPrevious = { onShopFlowEvent(ShopFlowEvent.ExitShopFlow) },
            previousButtonText = stringResource(id = R.string.cancel),
            previousButtonColors = MyFarmerTheme.buttonColors.error,
        )

        ShopFlowScaffoldType.STANDARD -> NavigationButtons(
            modifier = Modifier.padding(MyFarmerTheme.paddings.bottomButtons),
            onNext = { onShopFlowEvent(ShopFlowEvent.GoToNextStep) },
            onPrevious = { onShopFlowEvent(ShopFlowEvent.GoToPreviousStep) },
        )

        ShopFlowScaffoldType.SUBMIT -> NavigationButtons(
            modifier = Modifier.padding(MyFarmerTheme.paddings.bottomButtons),
            onNext = { onShopFlowEvent(ShopFlowEvent.Submit) },
            nextButtonText = stringResource(id = R.string.submit),
            onPrevious = { onShopFlowEvent(ShopFlowEvent.GoToPreviousStep) }
        )
    }

    BackHandler {
        when (type) {
            ShopFlowScaffoldType.INITIAL -> onShopFlowEvent(ShopFlowEvent.ExitShopFlow)
            ShopFlowScaffoldType.STANDARD -> onShopFlowEvent(ShopFlowEvent.GoToPreviousStep)
            ShopFlowScaffoldType.SUBMIT -> onShopFlowEvent(ShopFlowEvent.GoToPreviousStep)
        }
    }
}