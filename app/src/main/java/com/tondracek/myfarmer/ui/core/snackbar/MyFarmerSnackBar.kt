package com.tondracek.myfarmer.ui.core.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.components.toCardColors

object SnackBarType {
    const val ERROR = "ERROR"
    const val SUCCESS = "SUCCESS"
}

@Composable
fun MyFarmerSnackBar(
    snackbarData: SnackbarData
) {
    val type = snackbarData.visuals.actionLabel

    val cardColors = when (type) {
        SnackBarType.ERROR -> MyFarmerTheme.colors.errorContainer.toCardColors()
        SnackBarType.SUCCESS -> MyFarmerTheme.colors.successContainer.toCardColors()
        else -> MyFarmerTheme.colors.surfaceVariant.toCardColors()
    }

    SwipeToDismissBox(
        state = rememberSwipeToDismissBoxState(),
        onDismiss = { snackbarData.dismiss() },
        backgroundContent = {},
        content = {
            Card(
                shape = CircleShape,
                colors = cardColors,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MyFarmerTheme.paddings.small),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    when (type) {
                        SnackBarType.ERROR -> ErrorIcon()
                        SnackBarType.SUCCESS -> SuccessIcon()
                    }

                    Text(
                        modifier = Modifier.weight(1f),
                        text = snackbarData.visuals.message,
                        textAlign = TextAlign.Center,
                        style = MyFarmerTheme.typography.titleSmall,
                    )
                }
            }
        }
    )
}

@Composable
private fun ErrorIcon() {
    Icon(
        modifier = Modifier
            .background(MyFarmerTheme.colors.error, CircleShape)
            .padding(MyFarmerTheme.paddings.extraSmall),
        imageVector = Icons.Default.Close,
        contentDescription = null,
        tint = MyFarmerTheme.colors.onError,
    )
}

@Composable
private fun SuccessIcon() {
    Icon(
        modifier = Modifier
            .background(MyFarmerTheme.colors.success, CircleShape)
            .padding(MyFarmerTheme.paddings.extraSmall),
        imageVector = Icons.Default.Check,
        contentDescription = null,
        tint = MyFarmerTheme.colors.onSuccess,
    )
}