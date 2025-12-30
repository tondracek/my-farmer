package com.tondracek.myfarmer.ui.createshopflow.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.common.button.ButtonRow
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun NavigationButtons(
    modifier: Modifier = Modifier,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    submitMode: Boolean = false,
) {
    ButtonRow(modifier = modifier) {
        Button(
            modifier = Modifier.weight(1f),
            colors = MyFarmerTheme.buttonColors.tertiary,
            onClick = onPrevious
        ) {
            Text(stringResource(R.string.previous))
        }

        Button(
            modifier = Modifier.weight(1f),
            colors = MyFarmerTheme.buttonColors.success,
            onClick = onNext
        ) {
            val text = when (submitMode) {
                true -> stringResource(R.string.submit)
                false -> stringResource(R.string.next)
            }
            Text(text)
        }
    }
}