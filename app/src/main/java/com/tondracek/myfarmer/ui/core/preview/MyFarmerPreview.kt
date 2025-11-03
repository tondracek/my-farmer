package com.tondracek.myfarmer.ui.core.preview

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun MyFarmerPreview(
    content: @Composable () -> Unit
) {
    MyFarmerTheme {
        Scaffold { innerPadding ->

            Surface(
                modifier = Modifier.padding(innerPadding),
                color = MyFarmerTheme.colors.surfaceContainer
            ) {
                AsyncImagePreviewFix {
                    content()
                }
            }
        }
    }
}
