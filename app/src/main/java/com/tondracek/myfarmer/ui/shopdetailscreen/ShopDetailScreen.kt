package com.tondracek.myfarmer.ui.shopdetailscreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.tondracek.myfarmer.ui.common.layout.ErrorLayout
import com.tondracek.myfarmer.ui.common.layout.LoadingLayout
import com.tondracek.myfarmer.ui.common.layout.shopdetaillayout.ShopDetailState
import com.tondracek.myfarmer.ui.common.layout.shopdetaillayout.components.ShopDetailLayout
import com.tondracek.myfarmer.ui.core.appstate.LocalAppUiController

@Composable
fun ShopDetailScreen(
    state: ShopDetailState,
    navigateToReviews: () -> Unit,
) {
    val appUiController = LocalAppUiController.current

    when (state) {
        is ShopDetailState.Success -> {
            ShopDetailLayout(
                state = state,
                onReviewsClick = navigateToReviews,
            )
            LaunchedEffect(Unit) { appUiController.updateTopBarPadding(true) }
        }

        is ShopDetailState.Loading -> LoadingLayout()
        is ShopDetailState.Error -> ErrorLayout(text = "TODO :)")
    }
}
