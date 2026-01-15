package com.tondracek.myfarmer.ui.common.navbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tondracek.myfarmer.ui.core.navigation.isInNavGraph
import com.tondracek.myfarmer.ui.core.navigation.navigateToGraph
import com.tondracek.myfarmer.ui.core.preview.PreviewApi34
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun BottomNavigationBar(
    state: NavBarState,
    navController: NavController,
) {
    NavigationBar {
        navBarDestinations(state.isLoggedIn).forEach {
            val selected = navController.isInNavGraph(it.navGraph)
            NavigationBarButton(
                text = it.text,
                imageVector = it.imageVector,
                selected = selected,
                onClick = { navController.navigateToGraph(it.navGraph) }
            )
        }
    }
}

@Composable
private fun RowScope.NavigationBarButton(
    modifier: Modifier = Modifier,
    text: String,
    imageVector: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
) {
    NavigationBarItem(
        modifier = modifier,
        label = { Text(text) },
        icon = {
            Icon(
                imageVector = imageVector,
                contentDescription = text
            )
        },
        selected = selected,
        onClick = onClick,
    )
}

@PreviewApi34
@Composable
private fun BottomNavigationBarPreview() {
    MyFarmerTheme {
        BottomNavigationBar(
            state = NavBarState(isLoggedIn = true),
            navController = rememberNavController(),
        )
    }
}