package com.tondracek.myfarmer.ui.core.preview

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.tondracek.myfarmer.ui.core.appstate.AppUiController
import com.tondracek.myfarmer.ui.core.appstate.LocalAppUiController
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun MyFarmerPreview(
    useSurface: Boolean = true,
    content: @Composable () -> Unit,
) {
    @Composable
    fun view() {
        AsyncImagePreviewFix {
            val appUiController = remember { AppUiController() }

            CompositionLocalProvider(
                LocalAppUiController provides appUiController,
            ) {
                content()
            }
        }
    }

    MyFarmerTheme {
        when (useSurface) {
            true -> Surface(color = MyFarmerTheme.colors.surfaceContainer) {
                view()
            }

            false -> view()
        }
    }
}
