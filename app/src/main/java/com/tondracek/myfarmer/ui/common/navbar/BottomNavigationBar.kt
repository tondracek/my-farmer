package com.tondracek.myfarmer.ui.common.navbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.tondracek.myfarmer.ui.core.preview.PreviewApi34
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun BottomNavigationBar() {
    val viewmodel: NavBarViewModel = hiltViewModel()
    val state by viewmodel.state.collectAsState()

    NavigationBar {
        navBarDestinations(state.loggedIn).forEachIndexed { index, item ->
            NavigationBarButton(
                text = item.text,
                imageVector = item.imageVector,
                selected = state.selectedItem == index,
                onClick = { viewmodel.onItemSelected(index) }
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
        BottomNavigationBar()
    }
}