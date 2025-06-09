package com.example.myfarmer.shared.ui.topbar

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myfarmer.shared.theme.MyFarmerTheme
import com.example.myfarmer.shared.ui.iconbutton.ProfileIconButton
import com.example.myfarmer.shared.ui.iconbutton.ToggleSideMenuIconButton
import com.example.myfarmer.shared.ui.preview.PreviewApi34

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloatingTopBar(
    modifier: Modifier = Modifier,
    title: String,
    onToggleSideMenu: () -> Unit,
    onProfileClick: () -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        shape = RoundedCornerShape(64.dp),
        /* TODO: create a custom color scheme based on Material3 */
        color = CustomTopBarColors().containerColor,
        contentColor = CustomTopBarColors().titleContentColor,
        tonalElevation = 4.dp,
        shadowElevation = 4.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ToggleSideMenuIconButton(onToggleSideMenu)
            Text(
                modifier = Modifier
                    .weight(1f)
                    .animateContentSize(),
                text = title,
                textAlign = TextAlign.Center,
            )
            ProfileIconButton(onProfileClick)
        }
    }
}

@PreviewApi34
@Composable
fun RoundedTopBarPreview() {
    MyFarmerTheme {
        FloatingTopBar(
            title = "My Farmer",
            onToggleSideMenu = {},
            onProfileClick = {}
        )
    }
}