package com.tondracek.myfarmer.ui.common.navbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.core.navigation.Route

data class NavBarDestination(
    val text: String,
    val imageVector: ImageVector,
    val route: Route,
)

@Composable
fun navBarDestinations(loggedIn: Boolean): List<NavBarDestination> = listOfNotNull(
    NavBarDestination(
        text = stringResource(R.string.my_shops),
        imageVector = Icons.Default.Store,
        route = Route.MyShopsRoute,
    ).takeIf { loggedIn },
    NavBarDestination(
        text = stringResource(R.string.home),
        imageVector = Icons.Default.Home,
        route = Route.MainShopsScreenRoute,
    ),
    NavBarDestination(
        text = stringResource(R.string.profile),
        imageVector = Icons.Default.Person,
        route = Route.EditProfileScreenRoute,
    ).takeIf { loggedIn },
    NavBarDestination(
        text = stringResource(R.string.login),
        imageVector = Icons.Default.Person,
        route = Route.AuthScreenRoute,
    ).takeIf { !loggedIn }
)

