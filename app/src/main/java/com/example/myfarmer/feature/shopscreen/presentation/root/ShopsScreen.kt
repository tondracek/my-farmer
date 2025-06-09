package com.example.myfarmer.feature.shopscreen.presentation.root

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.myfarmer.R
import com.example.myfarmer.feature.shopscreen.presentation.listview.ShopsListViewState
import com.example.myfarmer.feature.shopscreen.presentation.mapview.ShopsMapView
import com.example.myfarmer.feature.shopscreen.presentation.mapview.ShopsMapViewState
import com.example.myfarmer.feature.shopscreen.presentation.root.components.ViewModeSwitcher
import com.example.myfarmer.shared.domain.ShopId
import com.example.myfarmer.shared.theme.MyFarmerTheme
import com.example.myfarmer.shared.ui.navbar.BottomNavigationBar
import com.example.myfarmer.shared.ui.preview.PreviewApi34
import com.example.myfarmer.shared.ui.topbar.FloatingTopBar
import kotlinx.coroutines.launch

enum class ShopsViewMode { Map, List }

private fun ShopsViewMode.toPage(): Int = this.ordinal
private fun Int.toShopsViewMode(): ShopsViewMode =
    ShopsViewMode.entries.getOrElse(this) { ShopsViewMode.Map }

private val pageCount = ShopsViewMode.entries.size

@Composable
fun ShopsScreen(
    mapViewState: ShopsMapViewState,
    onShopSelected: (ShopId?) -> Unit,
    listViewState: ShopsListViewState,
    navigateToShopDetail: (ShopId) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        pageCount = { pageCount },
        initialPage = ShopsViewMode.Map.toPage(),
    )

    fun switchViewMode(viewMode: ShopsViewMode) = coroutineScope.launch {
        pagerState.animateScrollToPage(viewMode.toPage())
    }

    val currentViewMode by remember {
        derivedStateOf { pagerState.currentPage.toShopsViewMode() }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        bottomBar = { BottomNavigationBar() }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false
            ) { page ->
                Content(
                    modifier = Modifier.fillMaxSize(),
                    selectedViewMode = page.toShopsViewMode(),
                    mapState = mapViewState,
                    onShopSelected = onShopSelected,
                    listViewState = listViewState,
                    navigateToShopDetail = navigateToShopDetail
                )
            }

            ViewModeSwitcher(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                selectedMode = currentViewMode,
                onMapClick = { switchViewMode(ShopsViewMode.Map) },
                onListClick = { switchViewMode(ShopsViewMode.List) },
            )

            FloatingTopBar(
                modifier = Modifier.align(Alignment.TopCenter),
                title = when (currentViewMode) {
                    ShopsViewMode.Map -> stringResource(R.string.shops_map)
                    ShopsViewMode.List -> stringResource(R.string.shops_list)
                },
                onToggleSideMenu = {},
                onProfileClick = {}
            )
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    selectedViewMode: ShopsViewMode,
    mapState: ShopsMapViewState,
    onShopSelected: (ShopId?) -> Unit,
    listViewState: ShopsListViewState,
    navigateToShopDetail: (ShopId) -> Unit,
) {
    when (selectedViewMode) {
        ShopsViewMode.Map -> ShopsMapView(
            modifier = modifier.fillMaxSize(),
            state = mapState,
            onShopSelected = onShopSelected,
        )

        ShopsViewMode.List -> ShopsListView(
            modifier = modifier.fillMaxSize(),
            listViewState = listViewState,
            navigateToShopDetail = navigateToShopDetail
        )
    }
}

@Composable
private fun ShopsListView(
    modifier: Modifier,
    listViewState: ShopsListViewState,
    navigateToShopDetail: (ShopId) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("List View Placeholder")
        Text("${listViewState.shops.size} shops")
        Button(onClick = { navigateToShopDetail("placeholder") }) {
            Text("Navigate to a Shop Detail")
        }
    }
}

@PreviewApi34
@Composable
private fun ShopScreenPreview() {
    MyFarmerTheme {
        ShopsScreen(
            mapViewState = ShopsMapViewState(),
            onShopSelected = {},
            listViewState = ShopsListViewState(),
            navigateToShopDetail = {},
        )
    }
}
