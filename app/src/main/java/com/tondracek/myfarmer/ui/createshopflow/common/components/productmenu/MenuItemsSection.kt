package com.tondracek.myfarmer.ui.createshopflow.common.components.productmenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.productmenu.domain.model.MenuItem
import com.tondracek.myfarmer.productmenu.domain.model.PriceLabel
import com.tondracek.myfarmer.productmenu.domain.model.ProductMenu
import com.tondracek.myfarmer.ui.common.lazycolumn.fadingEdges
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.createshopflow.common.components.MenuItemEdit

@Composable
fun MenuItemsSection(
    menu: ProductMenu,
    onAddMenuItem: (MenuItem) -> Unit,
    onEditMenuItem: (MenuItem) -> Unit,
    onDeleteMenuItem: (MenuItem) -> Unit,
) {
    var showAddMenuItemDialog by remember { mutableStateOf(false) }
    var editMenuItemDialog by remember { mutableStateOf<MenuItem?>(null) }

    Card(
        colors = MyFarmerTheme.cardColors.primary,
        shape = RoundedCornerShape(MyFarmerTheme.paddings.medium * 1.5f),
    ) {
        Column(
            modifier = Modifier.padding(MyFarmerTheme.paddings.medium),
        ) {

            Card(colors = MyFarmerTheme.cardColors.onPrimary) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MyFarmerTheme.paddings.medium),
                    text = stringResource(R.string.product_menu),
                    style = MyFarmerTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }

            ProductMenuList(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false),
                menu = menu,
                onEdit = { itemToEdit -> editMenuItemDialog = itemToEdit },
                onDelete = onDeleteMenuItem,
            )

            Button(onClick = { showAddMenuItemDialog = true }) {
                Text(stringResource(R.string.add_product))
            }
        }
    }

    if (showAddMenuItemDialog) {
        AddMenuItemDialog(
            onAdd = { newItem ->
                onAddMenuItem(newItem)
                showAddMenuItemDialog = false
            },
            onDismiss = { showAddMenuItemDialog = false }
        )
    }

    editMenuItemDialog?.let { itemToEdit ->
        EditMenuItemDialog(
            menuItem = itemToEdit,
            onUpdate = { updatedItem ->
                onEditMenuItem(updatedItem)
                editMenuItemDialog = null
            },
            onDismiss = { editMenuItemDialog = null }
        )
    }
}

@Composable
private fun ProductMenuList(
    modifier: Modifier = Modifier,
    menu: ProductMenu,
    onEdit: (MenuItem) -> Unit,
    onDelete: (MenuItem) -> Unit,
) {
    when {
        menu.items.isEmpty() -> Box(
            modifier = modifier,
            contentAlignment = Alignment.Center,
        ) {
            Text(
                modifier = Modifier.padding(MyFarmerTheme.paddings.medium),
                text = stringResource(R.string.no_products_yet),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        else -> {
            val listState = rememberLazyListState()
            LazyColumn(
                modifier = modifier.fadingEdges(
                    scrollableState = listState,
                    topEdgeHeight = MyFarmerTheme.paddings.medium,
                    bottomEdgeHeight = MyFarmerTheme.paddings.medium,
                ),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = MyFarmerTheme.paddings.medium),
            ) {
                items(menu.items) {
                    MenuItemEdit(it, onEdit, onDelete)
                }
            }
        }
    }
}

@Preview
@Composable
private fun MenuItemsSectionPreview() {
    val sampleMenu = ProductMenu(
        items = listOf(
            MenuItem(
                name = "Apples",
                description = "Fresh red apples",
                price = PriceLabel("1.99/kg"),
                inStock = false,
            ),
            MenuItem(
                name = "Bananas",
                description = "Ripe yellow bananas",
                price = PriceLabel("0.99/kg"),
                inStock = true,
            )
        )
    )

    MyFarmerPreview {
        MenuItemsSection(
            menu = sampleMenu,
            onAddMenuItem = { },
            onEditMenuItem = { },
            onDeleteMenuItem = { },
        )
    }
}

@Preview
@Composable
private fun MenuItemsSectionEmptyPreview() {
    val emptyMenu = ProductMenu(items = emptyList())

    MyFarmerPreview {
        MenuItemsSection(
            menu = emptyMenu,
            onAddMenuItem = { },
            onEditMenuItem = { },
            onDeleteMenuItem = { },
        )
    }
}