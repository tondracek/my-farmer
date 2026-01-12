package com.tondracek.myfarmer.ui.core.theme.myfarmertheme.components

import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme


data class MyFarmerTextFieldColors(
    val primary: TextFieldColors,
    val secondary: TextFieldColors,
)

val MyFarmerTheme.textFieldColors: MyFarmerTextFieldColors
    @Composable
    get() = MyFarmerTextFieldColors(
        primary = TextFieldDefaults.colors(
            focusedIndicatorColor = colors.onPrimary,
            unfocusedIndicatorColor = colors.onPrimaryContainer,
            disabledIndicatorColor = colors.onSurfaceVariant,
            errorIndicatorColor = colors.onError,

            focusedLabelColor = colors.onPrimary,
            unfocusedLabelColor = colors.onPrimaryContainer,
            disabledLabelColor = colors.onSurfaceVariant,
            errorLabelColor = colors.onError,

            cursorColor = colors.onPrimary,
            errorCursorColor = colors.onErrorContainer,

            focusedContainerColor = colors.primary,
            unfocusedContainerColor = colors.primaryContainer,
            disabledContainerColor = colors.surfaceVariant,
            errorContainerColor = colors.errorContainer,

            focusedTextColor = colors.onPrimary,
            unfocusedTextColor = colors.onPrimaryContainer,
            disabledTextColor = colors.onSurfaceVariant,
            errorTextColor = colors.onError,
        ),


        secondary = TextFieldDefaults.colors(
            focusedIndicatorColor = colors.onSecondary,
            unfocusedIndicatorColor = colors.onSecondaryContainer,
            disabledIndicatorColor = colors.onSurfaceVariant,
            errorIndicatorColor = colors.onError,

            focusedLabelColor = colors.onSecondary,
            unfocusedLabelColor = colors.onSecondaryContainer,
            disabledLabelColor = colors.onSurfaceVariant,
            errorLabelColor = colors.onError,

            cursorColor = colors.onSecondary,
            errorCursorColor = colors.onErrorContainer,

            focusedContainerColor = colors.secondary,
            unfocusedContainerColor = colors.secondaryContainer,
            disabledContainerColor = colors.surfaceVariant,
            errorContainerColor = colors.errorContainer
        ),
    )