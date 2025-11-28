package com.tondracek.myfarmer.ui.common.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun CardMessageLayout(
    modifier: Modifier = Modifier,
    cardModifier: Modifier = Modifier,
    cardColors: CardColors = MyFarmerTheme.cardColors.secondary,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Card(
            modifier = cardModifier.fillMaxWidth(0.75f),
            colors = cardColors,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                content()
            }
        }
    }
}

@Composable
fun CardSuccessMessageLayout(
    modifier: Modifier = Modifier,
    title: String,
) {
    CardMessageLayout(modifier = modifier) {
        Icon(
            modifier = Modifier.size(48.dp),
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Success icon",
            tint = MyFarmerTheme.colors.success,
        )
        Text(
            text = title,
            style = MyFarmerTheme.typography.textLarge,
        )
    }
}