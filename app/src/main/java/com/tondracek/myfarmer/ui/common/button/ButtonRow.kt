package com.tondracek.myfarmer.ui.common.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun ButtonRow(
    modifier: Modifier = Modifier,

    modifier1: Modifier = Modifier,
    onClick1: () -> Unit = {},
    buttonColors1: ButtonColors = MyFarmerTheme.buttonColors.primary,
    text1: String,

    modifier2: Modifier = Modifier,
    onClick2: () -> Unit = {},
    buttonColors2: ButtonColors = MyFarmerTheme.buttonColors.primary,
    text2: String,
) {
    ButtonRow(modifier = modifier) {
        Button(
            modifier = modifier1.weight(1f),
            colors = buttonColors1,
            onClick = onClick1
        ) {
            Text(text1)
        }

        Button(
            modifier = modifier2.weight(1f),
            colors = buttonColors2,
            onClick = onClick2
        ) {
            Text(text2)
        }
    }
}

@Composable
fun ButtonRow(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(MyFarmerTheme.paddings.bottomButtons),
        horizontalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.medium)
    ) {
        content()
    }
}