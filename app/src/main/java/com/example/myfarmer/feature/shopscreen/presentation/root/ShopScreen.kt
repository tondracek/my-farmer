package com.example.myfarmer.feature.shopscreen.presentation.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.myfarmer.R
import com.example.myfarmer.feature.shopscreen.presentation.common.ShopId
import com.example.myfarmer.feature.shopscreen.presentation.listview.ShopsListViewState
import com.example.myfarmer.feature.shopscreen.presentation.mapview.ShopsMapView
import com.example.myfarmer.feature.shopscreen.presentation.mapview.ShopsMapViewState
import com.example.myfarmer.feature.shopscreen.presentation.root.components.ViewModeSwitcher
import com.example.myfarmer.shared.theme.MyFarmerTheme
import com.example.myfarmer.shared.ui.navbar.BottomNavigationBar
import com.example.myfarmer.shared.ui.preview.PreviewApi34
import com.example.myfarmer.shared.ui.topbar.FloatingTopBar

@Composable
fun ShopScreen(
    /* Root resources */
    shopsScreenState: ShopsScreenState,
    onMapModeSelected: () -> Unit,
    onListModeSelected: () -> Unit,
    /* MapView recourses */
    mapState: ShopsMapViewState,
    onShopSelected: (ShopId?) -> Unit,
    /* ListView recourses */
    listViewState: ShopsListViewState,
) {
    Scaffold(
        bottomBar = { BottomNavigationBar() }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (shopsScreenState.viewMode) {
                ShopViewMode.Map -> ShopsMapView(
                    modifier = Modifier.fillMaxSize(),
                    state = mapState,
                    onShopSelected = onShopSelected,
                )

                ShopViewMode.List -> Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "List View Placeholder",
                )
            }

            ViewModeSwitcher(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                selectedMode = shopsScreenState.viewMode,
                onMapClick = onMapModeSelected,
                onListClick = onListModeSelected,
            )

            val topBarTitle = when (shopsScreenState.viewMode) {
                ShopViewMode.Map -> stringResource(R.string.shops_map)
                ShopViewMode.List -> stringResource(R.string.shops_list)
            }
            FloatingTopBar(
                modifier = Modifier.align(Alignment.TopCenter),
                title = topBarTitle,
                onToggleSideMenu = {},
                onProfileClick = {}
            )
        }
    }
}

@PreviewApi34
@Composable
private fun ShopScreenMapPreview() {
    MyFarmerTheme {
        ShopScreen(
            shopsScreenState = ShopsScreenState(viewMode = ShopViewMode.Map),
            onMapModeSelected = {},
            onListModeSelected = {},
            mapState = ShopsMapViewState(),
            onShopSelected = {},
            listViewState = ShopsListViewState(),
        )
    }
}

@PreviewApi34
@Composable
private fun ShopScreenListPreview() {
    MyFarmerTheme {
        ShopScreen(
            shopsScreenState = ShopsScreenState(viewMode = ShopViewMode.List),
            onMapModeSelected = {},
            onListModeSelected = {},
            mapState = ShopsMapViewState(),
            onShopSelected = {},
            listViewState = ShopsListViewState(),
        )
    }
}
