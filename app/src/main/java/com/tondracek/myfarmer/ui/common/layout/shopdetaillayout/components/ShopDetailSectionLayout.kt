package com.tondracek.myfarmer.ui.common.layout.shopdetaillayout.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun ShopDetailSectionLayout(
    modifier: Modifier = Modifier,
    title: String,
    list: List<@Composable () -> Unit>,
) {
    ShopDetailSectionLayout(
        modifier = modifier,
        title = title,
    ) {
        list.forEachIndexed { index, item ->
            item()
            if (index < list.size - 1)
                HorizontalDivider(Modifier.padding(horizontal = MyFarmerTheme.paddings.small))
        }
    }
}

@Composable
fun ShopDetailSectionLayout(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = MyFarmerTheme.cardColors.secondary,
    ) {
        Column(modifier = Modifier.padding(horizontal = MyFarmerTheme.paddings.small)) {
            Card(
                modifier = Modifier.padding(top = MyFarmerTheme.paddings.small),
                colors = MyFarmerTheme.cardColors.primary
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MyFarmerTheme.paddings.medium),
                    text = title,
                    style = MyFarmerTheme.typography.textLarge,
                    textAlign = TextAlign.Center
                )
            }
            content()
        }
    }
}