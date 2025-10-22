package com.tondracek.myfarmer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.tondracek.myfarmer.ui.core.theme.MyFarmerTheme
import com.tondracek.myfarmer.ui.shopscreen.ShopsScreenRoute
import com.tondracek.myfarmer.ui.shopscreen.shopsScreenDestination
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyFarmerTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = ShopsScreenRoute
                ) {
                    shopsScreenDestination()
                }
            }
        }
    }
}