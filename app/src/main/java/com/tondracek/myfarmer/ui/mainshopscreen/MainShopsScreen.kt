package com.tondracek.myfarmer.ui.mainshopscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.core.appstate.LocalAppUiController
import com.tondracek.myfarmer.ui.core.preview.PreviewApi34
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.mainshopscreen.components.ViewModeSwitcher
import kotlinx.coroutines.launch

enum class ShopsViewMode { Map, List }

fun ShopsViewMode.toPage(): Int = this.ordinal
fun Int.toShopsViewMode(): ShopsViewMode =
    ShopsViewMode.entries.getOrElse(this) { ShopsViewMode.Map }

@Composable
fun MainShopsScreen(
    state: MainShopsScreenState,
    mapView: @Composable (Modifier) -> Unit,
    listView: @Composable (Modifier) -> Unit,
    onOpenFiltersDialog: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        pageCount = { ShopsViewMode.entries.size },
        initialPage = ShopsViewMode.Map.toPage(),
    )

    val currentPage by remember {
        derivedStateOf { pagerState.currentPage }
    }

    fun switchPage(page: Int) = coroutineScope.launch {
        pagerState.animateScrollToPage(page)
    }

    MainShopsScreenWrapper(
        state = state,
        pagerState = pagerState,
        mapView = mapView,
        listView = listView,
        onSwitchMode = { switchPage(it.toPage()) },
        currentMode = currentPage.toShopsViewMode(),
        onOpenFiltersDialog = onOpenFiltersDialog,
    )

    val appUiStateController = LocalAppUiController.current

    val title = when (currentPage.toShopsViewMode()) {
        ShopsViewMode.Map -> stringResource(R.string.shops_map)
        ShopsViewMode.List -> stringResource(R.string.shops_list)
    }
    LaunchedEffect(title) { appUiStateController.updateTitle(title) }
}

@Composable
private fun MainShopsScreenWrapper(
    state: MainShopsScreenState,
    pagerState: PagerState,
    mapView: @Composable (Modifier) -> Unit,
    listView: @Composable (Modifier) -> Unit,
    onSwitchMode: (ShopsViewMode) -> Unit,
    currentMode: ShopsViewMode,
    onOpenFiltersDialog: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false
        ) { page ->
            when (page.toShopsViewMode()) {
                ShopsViewMode.Map -> mapView(Modifier.fillMaxSize())
                ShopsViewMode.List -> listView(Modifier.fillMaxSize())
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.extraSmall)
        ) {
            Surface(
                shape = RoundedCornerShape(64.dp),
                color = MyFarmerTheme.colors.surfaceContainer,
                tonalElevation = 4.dp,
            ) {
                Button(
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                    onClick = onOpenFiltersDialog,
                ) {
                    Row(
                        modifier = Modifier.padding(2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Open filters icon",
                        )
                        Text(stringResource(R.string.open_filters))
                    }
                }
            }
            ViewModeSwitcher(
                selectedMode = currentMode,
                onMapClick = { onSwitchMode(ShopsViewMode.Map) },
                onListClick = { onSwitchMode(ShopsViewMode.List) },
            )
        }
    }
}

@PreviewApi34
@Composable
private fun ShopScreenPreview() {
    MyFarmerTheme {
        MainShopsScreen(
            state = MainShopsScreenState(),
            mapView = {},
            listView = {},
            onOpenFiltersDialog = {},
        )
    }
}
