package com.tondracek.myfarmer.ui.common.divider

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun CustomHorizontalDivider(
    horizontalPadding: Dp = MyFarmerTheme.paddings.medium,
    verticalPadding: Dp = MyFarmerTheme.paddings.small,
    color: Color = LocalContentColor.current
) {
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = horizontalPadding,
                vertical = verticalPadding
            ),
        color = color,
    )
}