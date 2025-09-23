package com.tondracek.myfarmer.feature.shopscreen.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.feature.shopscreen.presentation.components.ViewModeSwitcher
import com.tondracek.myfarmer.shared.theme.MyFarmerTheme
import com.tondracek.myfarmer.shared.ui.navbar.BottomNavigationBar
import com.tondracek.myfarmer.shared.ui.preview.PreviewApi34
import com.tondracek.myfarmer.shared.ui.topbar.FloatingTopBar
import kotlinx.coroutines.launch

enum class ShopsViewMode { Map, List }

private fun ShopsViewMode.toPage(): Int = this.ordinal
private fun Int.toShopsViewMode(): ShopsViewMode =
    ShopsViewMode.entries.getOrElse(this) { ShopsViewMode.Map }

private val pageCount = ShopsViewMode.entries.size

@Composable
fun ShopsScreen(
    mapView: @Composable (Modifier) -> Unit,
    listView: @Composable (Modifier) -> Unit,
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
                when (page.toShopsViewMode()) {
                    ShopsViewMode.Map -> mapView(Modifier.fillMaxSize())
                    ShopsViewMode.List -> listView(Modifier.fillMaxSize())
                }
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

@PreviewApi34
@Composable
private fun ShopScreenPreview() {
    MyFarmerTheme {
        ShopsScreen(
            mapView = {},
            listView = {},
        )
    }
}
