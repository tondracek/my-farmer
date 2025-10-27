package com.tondracek.myfarmer.ui.shopdetailscreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tondracek.myfarmer.ui.common.layout.LoadingLayout
import com.tondracek.myfarmer.ui.common.layout.shopdetaillayout.ShopDetailLayout
import com.tondracek.myfarmer.ui.common.layout.shopdetaillayout.ShopDetailState

@Composable
fun ShopDetailScreen(
    state: ShopDetailState,
    navigateToReviews: () -> Unit,
) {
    when (state) {
        is ShopDetailState.Success -> SuccessScreen(
            state = state,
            navigateToReviews = navigateToReviews
        )

        is ShopDetailState.Loading -> LoadingScreen()
        is ShopDetailState.Error -> ErrorScreen()
    }
}

@Composable
private fun SuccessScreen(
    state: ShopDetailState.Success,
    navigateToReviews: () -> Unit,
) {
    Scaffold { innerPaddings ->
        ShopDetailLayout(
            modifier = Modifier.padding(innerPaddings),
            state = state,
            onReviewsClick = navigateToReviews,
        )
    }
}

@Composable
private fun LoadingScreen(modifier: Modifier = Modifier) {
    Scaffold { innerPaddings ->
        LoadingLayout(modifier.padding(innerPaddings))
    }
}

@Composable
private fun ErrorScreen() {
    Scaffold { innerPaddings ->
        Text(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddings),
            text = "TODO :)"
        )
    }
}