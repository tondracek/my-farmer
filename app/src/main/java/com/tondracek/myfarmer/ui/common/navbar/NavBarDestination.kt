package com.tondracek.myfarmer.ui.common.navbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.core.navigation.AppNavigator
import com.tondracek.myfarmer.ui.editprofilescreen.navigateToEditProfileScreen
import com.tondracek.myfarmer.ui.mainshopscreen.navigateToMainShopScreen
import com.tondracek.myfarmer.ui.myshopsscreen.navigateToMyShopsScreen

data class NavBarDestination(
    val text: String,
    val imageVector: ImageVector,
    val onNavigate: (AppNavigator) -> Unit,
)

@Composable
fun navBarDestinations(loggedIn: Boolean): List<NavBarDestination> = listOfNotNull(
    NavBarDestination(
        text = stringResource(R.string.my_shops),
        imageVector = Icons.Default.Store,
        onNavigate = { it.navigateToMyShopsScreen() }
    ).takeIf { loggedIn },
    NavBarDestination(
        text = stringResource(R.string.home),
        imageVector = Icons.Default.Home,
        onNavigate = { it.navigateToMainShopScreen() }
    ),
    NavBarDestination(
        text = stringResource(R.string.profile),
        imageVector = Icons.Default.Person,
        onNavigate = { it.navigateToEditProfileScreen() }
    ).takeIf { loggedIn },
    NavBarDestination(
        text = stringResource(R.string.login),
        imageVector = Icons.Default.Person,
        onNavigate = { it.navigateToEditProfileScreen() }
    )
)

