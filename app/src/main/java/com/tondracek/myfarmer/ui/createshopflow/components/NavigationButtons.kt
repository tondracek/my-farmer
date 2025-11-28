package com.tondracek.myfarmer.ui.createshopflow.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun NavigationButtons(
    modifier: Modifier = Modifier,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    submitMode: Boolean = false,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.medium)
    ) {
        Button(
            modifier = Modifier.weight(1f),
            colors = MyFarmerTheme.buttonColors.tertiary,
            onClick = onPrevious
        ) {
            Text("Previous")
        }

        Button(
            modifier = Modifier.weight(1f),
            colors = MyFarmerTheme.buttonColors.success,
            onClick = onNext
        ) {
            Text(if (submitMode) "Submit" else "Next")
        }
    }
}