package com.example.myfarmer.shared.ui.navbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.myfarmer.shared.theme.MyFarmerTheme
import com.example.myfarmer.shared.ui.preview.PreviewApi34


@Composable
fun BottomNavigationBar() {
    var selectedItem by remember { mutableIntStateOf(0) }

    NavigationBar {
        NavigationBarButton(
            text = "TODO",
            imageVector = Icons.Default.Check,
            selected = selectedItem == 0,
            onClick = { selectedItem = 0 }
        )
        NavigationBarButton(
            text = "Home",
            imageVector = Icons.Default.Home,
            selected = selectedItem == 1,
            onClick = { selectedItem = 1 }
        )
        NavigationBarButton(
            text = "TODO",
            imageVector = Icons.Default.Check,
            selected = selectedItem == 2,
            onClick = { selectedItem = 2 }
        )
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