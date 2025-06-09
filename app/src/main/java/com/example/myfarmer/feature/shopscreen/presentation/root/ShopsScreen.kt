package com.example.myfarmer.feature.shopscreen.presentation.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
fun ShopsScreen(
    /* Root resources */
    shopsScreenState: ShopsScreenState,
    onViewModeSelected: (ShopsViewMode) -> Unit,
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
            Content(
                modifier = Modifier.fillMaxSize(),
                shopsScreenState = shopsScreenState,
                onPageChanged = onViewModeSelected,
                mapState = mapState,
                onShopSelected = onShopSelected
            )

            ViewModeSwitcher(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                selectedMode = shopsScreenState.viewMode,
                onMapClick = { onViewModeSelected(ShopsViewMode.Map) },
                onListClick = { onViewModeSelected(ShopsViewMode.List) },
            )

            val topBarTitle = when (shopsScreenState.viewMode) {
                ShopsViewMode.Map -> stringResource(R.string.shops_map)
                ShopsViewMode.List -> stringResource(R.string.shops_list)
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

@Composable
private fun Content(
    modifier: Modifier,
    shopsScreenState: ShopsScreenState,
    onPageChanged: (ShopsViewMode) -> Unit,
    mapState: ShopsMapViewState,
    onShopSelected: (ShopId?) -> Unit
) {
    fun ShopsViewMode.toPage(): Int = this.ordinal
    fun Int.toShopsViewMode(): ShopsViewMode = ShopsViewMode.entries[this]

    val pagerState = rememberPagerState(
        pageCount = { 2 },
        initialPage = shopsScreenState.viewMode.toPage(),
    )

    LaunchedEffect(shopsScreenState.viewMode) {
        if (shopsScreenState.viewMode == pagerState.currentPage.toShopsViewMode()) return@LaunchedEffect
        pagerState.animateScrollToPage(shopsScreenState.viewMode.toPage())
    }

    LaunchedEffect(pagerState.currentPage) {
        if (shopsScreenState.viewMode == pagerState.currentPage.toShopsViewMode()) return@LaunchedEffect
        onPageChanged(pagerState.currentPage.toShopsViewMode())
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize(),
    ) { page ->
        when (page) {
            ShopsViewMode.Map.toPage() -> ShopsMapView(
                modifier = Modifier.fillMaxSize(),
                state = mapState,
                onShopSelected = onShopSelected,
            )

            ShopsViewMode.List.toPage() -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { Text("List View Placeholder") }
        }
    }
}

@PreviewApi34
@Composable
private fun ShopScreenMapPreview() {
    MyFarmerTheme {
        ShopsScreen(
            shopsScreenState = ShopsScreenState(viewMode = ShopsViewMode.Map),
            onViewModeSelected = {},
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
        ShopsScreen(
            shopsScreenState = ShopsScreenState(viewMode = ShopsViewMode.List),
            onViewModeSelected = {},
            mapState = ShopsMapViewState(),
            onShopSelected = {},
            listViewState = ShopsListViewState(),
        )
    }
}
