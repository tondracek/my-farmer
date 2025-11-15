package com.tondracek.myfarmer.ui.core.theme.myfarmertheme.components

import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

data class MyFarmerCardColors(
    val base: CardColors,
    val primary: CardColors,
    val secondary: CardColors,
)

val myFarmerCardColors
    @Composable get() = MyFarmerCardColors(
        base = CardDefaults.cardColors(),
        primary = CardDefaults.cardColors(
            containerColor = MyFarmerTheme.colors.primaryContainer,
            contentColor = MyFarmerTheme.colors.onPrimaryContainer
        ),
        secondary = CardDefaults.cardColors(
            containerColor = MyFarmerTheme.colors.secondaryContainer,
            contentColor = MyFarmerTheme.colors.onSecondaryContainer
        ),
    )