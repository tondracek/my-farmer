package com.tondracek.myfarmer.ui.common.topbar

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import com.tondracek.myfarmer.ui.core.preview.PreviewApi34
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloatingTopBar(
    modifier: Modifier = Modifier,
    title: String,
    leftIconContent: @Composable () -> Unit = {},
    rightIconContent: @Composable () -> Unit = {},
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        shape = RoundedCornerShape(64.dp),
        color = customTopBarColors().containerColor,
        contentColor = customTopBarColors().titleContentColor,
        tonalElevation = 4.dp,
        shadowElevation = 4.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .heightIn(min = 48.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            leftIconContent()
            Text(
                modifier = Modifier
                    .weight(1f)
                    .animateContentSize(),
                text = title,
                textAlign = TextAlign.Center,
                style = MyFarmerTheme.typography.topbarTitle,
            )
            rightIconContent()
        }
    }
}

@PreviewApi34
@Composable
private fun RoundedTopBarPreview() {
    MyFarmerTheme {
        FloatingTopBar(
            title = "My Farmer",
        )
    }
}