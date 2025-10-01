package com.tondracek.myfarmer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.tondracek.myfarmer.shared.theme.MyFarmerTheme
import com.tondracek.myfarmer.ui.demo.DemoScreenRoute
import com.tondracek.myfarmer.ui.demo.demoDestination
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
                    startDestination = DemoScreenRoute
                ) {
                    demoDestination()
                }
            }
        }
    }
}