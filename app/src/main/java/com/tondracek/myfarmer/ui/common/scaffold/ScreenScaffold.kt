package com.tondracek.myfarmer.ui.common.scaffold

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.common.topbar.FloatingTopBar

@Composable
fun ScreenScaffold(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.app_name),

    showTopBar: Boolean = true,
    applyContentPaddingInternally: Boolean = true,

    bottomBar: @Composable () -> Unit = {},

    leftIconContent: @Composable (() -> Unit) = {},
    rightIconContent: @Composable (() -> Unit) = {},

    content: @Composable (contentPadding: PaddingValues) -> Unit,
) {
    val localDensity = LocalDensity.current

    var topContentPadding by remember { mutableStateOf(0.dp) }
    var bottomContentPadding by remember { mutableStateOf(0.dp) }

    val contentPadding by remember(topContentPadding, bottomContentPadding) {
        val paddingValues = PaddingValues(top = topContentPadding, bottom = bottomContentPadding)
        mutableStateOf(paddingValues)
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (applyContentPaddingInternally) contentPadding else PaddingValues(0.dp)),
            contentAlignment = Alignment.Center,
        ) {
            content(contentPadding)
        }

        if (showTopBar)
            FloatingTopBar(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .onGloballyPositioned { coordinates ->
                        with(localDensity) { topContentPadding = coordinates.size.height.toDp() }
                    },
                title = title,
                rightIconContent = rightIconContent,
                leftIconContent = leftIconContent,
            )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .onGloballyPositioned { coordinates ->
                    with(localDensity) { bottomContentPadding = coordinates.size.height.toDp() }
                },
        ) {
            bottomBar()
        }
    }
}