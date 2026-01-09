package com.tondracek.myfarmer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.stefanoq21.material3.navigation.ModalBottomSheetLayout
import com.stefanoq21.material3.navigation.rememberBottomSheetNavigator
import com.tondracek.myfarmer.ui.authscreen.authScreenDestination
import com.tondracek.myfarmer.ui.common.scaffold.AppScaffold
import com.tondracek.myfarmer.ui.core.appstate.AppUiController
import com.tondracek.myfarmer.ui.core.navigation.NavGraph
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.createshopflow.components.addcategorydialog.addCategoryDialogDestination
import com.tondracek.myfarmer.ui.createshopflow.flowcreate.createShopDestination
import com.tondracek.myfarmer.ui.createshopflow.flowupdate.updateShopDestination
import com.tondracek.myfarmer.ui.editprofilescreen.editProfileDestination
import com.tondracek.myfarmer.ui.mainshopscreen.mainShopsScreenDestination
import com.tondracek.myfarmer.ui.myshopsscreen.myShopsScreenDestination
import com.tondracek.myfarmer.ui.reviewscreen.shopReviewsScreenDestination
import com.tondracek.myfarmer.ui.shopbottomsheet.shopBottomSheetDestination
import com.tondracek.myfarmer.ui.shopdetailscreen.shopDetailScreenDestination
import com.tondracek.myfarmer.ui.shopfilterdialog.shopsFilterDialogDestination
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())

        setContent {
            MyFarmerTheme {
                val bottomSheetNavigator = rememberBottomSheetNavigator()

                val navController = rememberNavController(bottomSheetNavigator)
                val appUiController = remember { AppUiController() }

                AppScaffold(navController, appUiController) {
                    ModalBottomSheetLayout(
                        modifier = Modifier.fillMaxSize(),
                        bottomSheetNavigator = bottomSheetNavigator,
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = NavGraph.MainFlow,
                        ) {
                            navigation<NavGraph.MainFlow>(NavGraph.MainFlow.Home) {
                                navigation<NavGraph.MainFlow.MyShops>(Route.MyShopsRoute) {
                                    myShopsScreenDestination(navController)
                                }

                                navigation<NavGraph.MainFlow.Home>(Route.MainShopsRoute) {
                                    mainShopsScreenDestination(navController)
                                    shopBottomSheetDestination(navController, appUiController)
                                }

                                navigation<NavGraph.MainFlow.Profile>(Route.EditProfileScreenRoute) {
                                    editProfileDestination(navController)
                                }

                                navigation<NavGraph.MainFlow.Auth>(Route.AuthScreenRoute) {
                                    authScreenDestination(navController)
                                }
                            }

                            shopDetailScreenDestination(navController)
                            shopReviewsScreenDestination(navController)

                            createShopDestination(navController)
                            updateShopDestination(navController)

                            addCategoryDialogDestination(navController)
                            shopsFilterDialogDestination(navController)
                        }
                    }
                }
            }
        }
    }
}