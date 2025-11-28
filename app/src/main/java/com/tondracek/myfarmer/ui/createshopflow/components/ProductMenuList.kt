package com.tondracek.myfarmer.ui.createshopflow.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.productmenu.domain.model.MenuItem
import com.tondracek.myfarmer.productmenu.domain.model.ProductMenu
import com.tondracek.myfarmer.shop.data.shop0
import com.tondracek.myfarmer.ui.common.productmenu.InStockLabel
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun ProductMenuList(
    menu: ProductMenu,
    onEdit: (MenuItem) -> Unit,
    onDelete: (MenuItem) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (menu.items.isEmpty()) {
            Text("No products yet", color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            menu.items.forEach { item ->
                MenuItem(item, onEdit, onDelete)
            }
        }
    }
}

@Composable
private fun MenuItem(
    item: MenuItem,
    onEdit: (MenuItem) -> Unit,
    onDelete: (MenuItem) -> Unit
) {
    Card(colors = MyFarmerTheme.cardColors.secondary) {
        Row {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(item.name, style = MyFarmerTheme.typography.textMedium)
                    Text(text = item.price.toString(), style = MyFarmerTheme.typography.textSmall)
                }

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = MyFarmerTheme.paddings.extraLarge),
                    color = LocalContentColor.current
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )

                InStockLabel(inStock = item.inStock)
            }

            Column {
                IconButton(onClick = { onEdit(item) }) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                }
                IconButton(onClick = { onDelete(item) }) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProductMenuListPreview() {
    val sampleMenu = shop0.menu

    MyFarmerPreview {
        ProductMenuList(
            menu = sampleMenu,
            onEdit = {},
            onDelete = {}
        )
    }
}