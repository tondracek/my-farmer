package com.tondracek.myfarmer.ui.myshopsscreen

import com.tondracek.myfarmer.ui.core.navigation.AppNavigator
import kotlinx.serialization.Serializable

@Serializable
data object MyShopsDestination

fun AppNavigator.navigateToMyShopsScreen() =
    navigate(MyShopsDestination)

