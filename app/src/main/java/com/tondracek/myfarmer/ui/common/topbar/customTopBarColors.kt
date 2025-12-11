package com.tondracek.myfarmer.ui.common.topbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun customTopBarColors() = TopAppBarDefaults.topAppBarColors(
    containerColor = MyFarmerTheme.colors.primaryContainer,
    titleContentColor = MyFarmerTheme.colors.onPrimaryContainer,
    navigationIconContentColor = MyFarmerTheme.colors.onPrimaryContainer,
    actionIconContentColor = MyFarmerTheme.colors.onPrimaryContainer,
)