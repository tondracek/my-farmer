package com.tondracek.myfarmer.ui.common.navbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.tondracek.myfarmer.ui.core.navigation.Route
import com.tondracek.myfarmer.ui.core.preview.PreviewApi34
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun BottomNavigationBar(
    state: NavBarState,
    onItemSelected: (Int) -> Unit,
    onNavigate: (Route) -> Unit,
) {
    NavigationBar {
        navBarDestinations(state.loggedIn).forEachIndexed { index, item ->
            NavigationBarButton(
                text = item.text,
                imageVector = item.imageVector,
                selected = state.selectedItem == index,
                onClick = {
                    onItemSelected(index)
                    onNavigate(item.route)
                }
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
            state = NavBarState(
                selectedItem = 1,
                loggedIn = true,
            ),
            onItemSelected = {},
            onNavigate = {},
        )
    }
}