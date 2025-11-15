package com.tondracek.myfarmer.ui.core.preview

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
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
                content()
            }
        }
    }
}
