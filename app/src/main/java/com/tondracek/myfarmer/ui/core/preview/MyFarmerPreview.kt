package com.tondracek.myfarmer.ui.core.preview

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.tondracek.myfarmer.ui.core.appstate.AppUiController
import com.tondracek.myfarmer.ui.core.appstate.AppUiState
import com.tondracek.myfarmer.ui.core.appstate.LocalAppUiController
import com.tondracek.myfarmer.ui.core.appstate.LocalAppUiState
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun MyFarmerPreview(
    content: @Composable () -> Unit
) {
    MyFarmerTheme {
        Surface(
            color = MyFarmerTheme.colors.surfaceContainer
        ) {
            AsyncImagePreviewFix {
                var appUiState by remember { mutableStateOf(AppUiState()) }
                val appUiController = remember {
                    object : AppUiController {
                        override fun updateTitle(title: String) = apply {
                            appUiState = appUiState.copy(title = title)
                        }

                        override fun updateTopBarPadding(applyPadding: Boolean) = apply {
                            appUiState = appUiState.copy(applyTopBarPadding = applyPadding)
                        }
                    }
                }

                CompositionLocalProvider(
                    LocalAppUiState provides appUiState,
                    LocalAppUiController provides appUiController,
                ) {
                    content()
                }
            }
        }
    }
}
