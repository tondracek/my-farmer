package com.tondracek.myfarmer.ui.core.theme.myfarmertheme.components

import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tondracek.myfarmer.ui.common.color.contrastColor
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

data class MyFarmerCardColors(
    val base: CardColors,

    val primary: CardColors,
    val onPrimary: CardColors,

    val secondary: CardColors,
    val onSecondary: CardColors,

    val tertiary: CardColors,

    val error: CardColors,

    val custom: @Composable (containerColor: Color) -> CardColors
) {

    @Composable
    fun next(cardColors: CardColors): CardColors = when (cardColors) {
        MyFarmerTheme.cardColors.base -> MyFarmerTheme.cardColors.primary

        MyFarmerTheme.cardColors.primary -> MyFarmerTheme.cardColors.onPrimary
        MyFarmerTheme.cardColors.onPrimary -> MyFarmerTheme.cardColors.secondary

        MyFarmerTheme.cardColors.secondary -> MyFarmerTheme.cardColors.onSecondary
        MyFarmerTheme.cardColors.onSecondary -> MyFarmerTheme.cardColors.tertiary

        MyFarmerTheme.cardColors.tertiary -> MyFarmerTheme.cardColors.base

        else -> cardColors
    }
}

val myFarmerCardColors
    @Composable get() = MyFarmerCardColors(
        base = CardDefaults.cardColors(),

        primary = CardDefaults.cardColors(
            containerColor = MyFarmerTheme.colors.primaryContainer,
            contentColor = MyFarmerTheme.colors.onPrimaryContainer
        ),
        onPrimary = CardDefaults.cardColors(
            containerColor = MyFarmerTheme.colors.primary,
            contentColor = MyFarmerTheme.colors.onPrimary,
        ),

        secondary = CardDefaults.cardColors(
            containerColor = MyFarmerTheme.colors.secondaryContainer,
            contentColor = MyFarmerTheme.colors.onSecondaryContainer
        ),
        onSecondary = CardDefaults.cardColors(
            containerColor = MyFarmerTheme.colors.secondary,
            contentColor = MyFarmerTheme.colors.onSecondary,
        ),

        tertiary = CardDefaults.cardColors(
            containerColor = MyFarmerTheme.colors.tertiaryContainer,
            contentColor = MyFarmerTheme.colors.onTertiaryContainer
        ),

        error = CardDefaults.cardColors(
            containerColor = MyFarmerTheme.colors.errorContainer,
            contentColor = MyFarmerTheme.colors.onErrorContainer
        ),
        custom = { containerColor ->
            CardDefaults.cardColors(
                containerColor = containerColor,
                contentColor = contrastColor(containerColor)
            )
        }
    )