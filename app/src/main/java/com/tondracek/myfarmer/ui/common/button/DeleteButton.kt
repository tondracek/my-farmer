package com.tondracek.myfarmer.ui.common.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.preview.PreviewDark
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun DeleteButton(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit),
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = MyFarmerTheme.buttonColors.error,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
            )
            Text(text = stringResource(R.string.delete))
        }
    }
}

@PreviewDark
@Composable
private fun DeleteButtonPreview() {
    MyFarmerPreview {
        DeleteButton(
            onClick = {}
        )
    }
}