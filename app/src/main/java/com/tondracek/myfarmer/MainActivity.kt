package com.tondracek.myfarmer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.stefanoq21.material3.navigation.ModalBottomSheetLayout
import com.stefanoq21.material3.navigation.rememberBottomSheetNavigator
import com.tondracek.myfarmer.review.data.ReviewRepository
import com.tondracek.myfarmer.shop.data.ShopRepository
import com.tondracek.myfarmer.systemuser.data.UserRepository
import com.tondracek.myfarmer.ui.authscreen.authScreenDestination
import com.tondracek.myfarmer.ui.core.appstate.AppScaffold
import com.tondracek.myfarmer.ui.core.navigation.AppNavigator
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.createshopflow.flowcreate.createShopDestination
import com.tondracek.myfarmer.ui.createshopflow.flowupdate.updateShopDestination
import com.tondracek.myfarmer.ui.editprofilescreen.editProfileDestination
import com.tondracek.myfarmer.ui.mainshopscreen.mainShopsScreenDestination
import com.tondracek.myfarmer.ui.myshopsscreen.myShopsScreenDestination
import com.tondracek.myfarmer.ui.shopbottomsheet.shopBottomSheetDestination
import com.tondracek.myfarmer.ui.shopdetailscreen.shopDetailScreenDestination
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var shopRepository: ShopRepository

    @Inject
    lateinit var reviewRepository: ReviewRepository

    @Inject
    lateinit var navigator: AppNavigator

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyFarmerTheme {
                val bottomSheetNavigator = rememberBottomSheetNavigator()
                val navController = rememberNavController(bottomSheetNavigator)
                navigator.navController = navController

                AppScaffold {
                    ModalBottomSheetLayout(
                        modifier = Modifier.fillMaxSize(),
                        bottomSheetNavigator = bottomSheetNavigator,
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = Route.MainShopsRoute
                        ) {
                            mainShopsScreenDestination()
                            shopBottomSheetDestination()
                            shopDetailScreenDestination()
                            editProfileDestination()
                            authScreenDestination()
                            myShopsScreenDestination()

                            createShopDestination()
                            updateShopDestination()
                        }
                    }
                }
            }
        }

//        GlobalScope.launch {
//            sampleUsers.forEach { userRepository.create(it) }
//            sampleShops.forEach { shopRepository.create(it) }
//            sampleReviews.forEach { reviewRepository.create(it) }
//        }
    }
}