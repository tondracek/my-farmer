package com.tondracek.myfarmer.ui.common.scaffold

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
    applyTopBarPadding: Boolean = true,
    leftIconContent: @Composable (() -> Unit) = {},
    rightIconContent: @Composable (() -> Unit) = {},
    content: @Composable BoxScope.() -> Unit,
) {
    val localDensity = LocalDensity.current

    var topContentPadding by remember { mutableStateOf(0.dp) }
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topContentPadding),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }

        if (showTopBar)
            FloatingTopBar(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .onGloballyPositioned { coordinates ->
                        with(localDensity) {
                            if (applyTopBarPadding)
                                topContentPadding = coordinates.size.height.toDp()
                        }
                    },
                title = title,
                rightIconContent = rightIconContent,
                leftIconContent = leftIconContent,
            )
    }
}