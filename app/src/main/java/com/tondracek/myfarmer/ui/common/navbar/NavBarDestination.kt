package com.tondracek.myfarmer.ui.common.navbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.core.navigation.NavGraph

data class NavBarDestination(
    val text: String,
    val imageVector: ImageVector,
    val navGraph: NavGraph,
)

@Composable
fun navBarDestinations(loggedIn: Boolean): List<NavBarDestination> = listOfNotNull(
    NavBarDestination(
        text = stringResource(R.string.my_shops),
        imageVector = Icons.Default.Store,
        navGraph = NavGraph.MainFlow.MyShops,
    ).takeIf { loggedIn },
    NavBarDestination(
        text = stringResource(R.string.home),
        imageVector = Icons.Default.Home,
        navGraph = NavGraph.MainFlow.Home
    ),
    NavBarDestination(
        text = stringResource(R.string.profile),
        imageVector = Icons.Default.Person,
        navGraph = NavGraph.MainFlow.Profile
    ).takeIf { loggedIn },
    NavBarDestination(
        text = stringResource(R.string.login),
        imageVector = Icons.Default.Person,
        navGraph = NavGraph.MainFlow.Auth
    ).takeIf { !loggedIn }
)
