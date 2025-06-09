package com.example.myfarmer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.myfarmer.feature.shopscreen.presentation.ShopScreenRoute
import com.example.myfarmer.feature.shopscreen.presentation.shopScreenDestination
import com.example.myfarmer.shared.theme.MyFarmerTheme
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
                    startDestination = ShopScreenRoute
                ) {
                    shopScreenDestination()
                }
            }
        }
    }
}