package com.tondracek.myfarmer.ui.createshopflow.common.components.scaffold

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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
    nextButtonText: String = stringResource(R.string.next),
    nextButtonColors: ButtonColors = MyFarmerTheme.buttonColors.primary,
    onPrevious: () -> Unit,
    previousButtonText: String = stringResource(R.string.previous),
    previousButtonColors: ButtonColors = MyFarmerTheme.buttonColors.tertiary,
) {
    ButtonRow(modifier = modifier) {
        Button(
            modifier = Modifier.weight(1f),
            colors = previousButtonColors,
            onClick = onPrevious
        ) {
            Text(previousButtonText)
        }

        Button(
            modifier = Modifier.weight(1f),
            colors = nextButtonColors,
            onClick = onNext
        ) {
            Text(nextButtonText)
        }
    }
}