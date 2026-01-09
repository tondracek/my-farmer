package com.tondracek.myfarmer.ui.common.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun GoBackButton(
    text: String = stringResource(R.string.go_back),
    buttonColors: ButtonColors = ButtonDefaults.buttonColors(),
    onNavigateBack: () -> Unit,
) {
    Button(
        colors = buttonColors,
        onClick = onNavigateBack
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.small)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Go Back Icon",
            )
            Text(text = text)
        }
    }
}

@Preview
@Composable
fun GoBackButtonPreview() {
    MyFarmerPreview {
        GoBackButton {}
    }
}