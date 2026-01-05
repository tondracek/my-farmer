package com.tondracek.myfarmer.ui.core.uievents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.components.toCardColors

@Composable
fun MyFarmerSnackBar(
    snackbarData: SnackbarData
) {
    Card(
        shape = CircleShape,
        colors = MyFarmerTheme.colors.errorContainer.toCardColors(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MyFarmerTheme.paddings.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier
                    .background(
                        MyFarmerTheme.colors.error,
                        shape = CircleShape
                    )
                    .padding(MyFarmerTheme.paddings.extraSmall),
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = MyFarmerTheme.colors.onError,
            )

            Text(
                modifier = Modifier.weight(1f),
                text = snackbarData.visuals.message,
                textAlign = TextAlign.Center,
                style = MyFarmerTheme.typography.titleSmall,
            )
        }
    }
}