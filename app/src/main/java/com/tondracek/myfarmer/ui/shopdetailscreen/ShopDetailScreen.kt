package com.tondracek.myfarmer.ui.shopdetailscreen

import androidx.compose.runtime.Composable
import com.tondracek.myfarmer.ui.common.layout.LoadingLayout
import com.tondracek.myfarmer.ui.shopdetailscreen.components.ShopDetailLayout

@Composable
fun ShopDetailScreen(
    state: ShopDetailState,
    navigateToReviews: () -> Unit,
    showErrorMessage: (String) -> Unit,
) {
    when (state) {
        is ShopDetailState.Success -> ShopDetailLayout(
            state = state,
            onReviewsClick = navigateToReviews,
            showErrorMessage = showErrorMessage,
        )

        is ShopDetailState.Loading -> LoadingLayout()
    }
}
