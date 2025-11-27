package com.tondracek.myfarmer.ui.common.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.ui.common.button.GoBackButton
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun ErrorLayout(
    modifier: Modifier = Modifier,
    failure: UCResult.Failure,
    onNavigateBack: () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(colors = MyFarmerTheme.cardColors.error) {
            Column(
                modifier = Modifier.padding(MyFarmerTheme.paddings.medium),
                verticalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.small),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    modifier = Modifier.size(48.dp),
                    imageVector = Icons.Default.Error,
                    contentDescription = "Error Icon",
                    tint = MyFarmerTheme.colors.error,
                )
                Text(
                    modifier = Modifier.widthIn(max = 270.dp),
                    text = failure.userError,
                    textAlign = TextAlign.Center,
                )
                GoBackButton(onNavigateBack = onNavigateBack)
            }
        }
    }
}

@Preview
@Composable
private fun ErrorLayoutPreview() {
    MyFarmerPreview {
        ErrorLayout(
            failure = UCResult.Failure(
                userError = "An unexpected error occurred. Please try again later.",
                systemError = "NullPointerException at line 42",
            ),
            onNavigateBack = {}
        )
    }
}