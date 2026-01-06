package com.tondracek.myfarmer.ui.shopdetailscreen

import androidx.compose.runtime.Composable
import com.tondracek.myfarmer.ui.common.layout.ErrorLayout
import com.tondracek.myfarmer.ui.common.layout.LoadingLayout
import com.tondracek.myfarmer.ui.shopdetailscreen.components.ShopDetailLayout

@Composable
fun ShopDetailScreen(
    state: ShopDetailState,
    navigateToReviews: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    when (state) {
        is ShopDetailState.Success -> ShopDetailLayout(
            state = state,
            onReviewsClick = navigateToReviews,
        )

        is ShopDetailState.Loading -> LoadingLayout()
        is ShopDetailState.Error -> ErrorLayout(
            failure = state.result,
            onNavigateBack = onNavigateBack
        )
    }
}
