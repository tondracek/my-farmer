package com.tondracek.myfarmer.ui.createshopflow.components.productmenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.tondracek.myfarmer.productmenu.domain.model.MenuItem
import com.tondracek.myfarmer.productmenu.domain.model.ProductMenu
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.createshopflow.components.ProductMenuList

@Composable
fun MenuItemsSection(
    menu: ProductMenu,
    onUpdateMenu: (ProductMenu) -> Unit,
) {
    var showAddMenuItemDialog by remember { mutableStateOf(false) }
    var editMenuItemDialog by remember { mutableStateOf<MenuItem?>(null) }

    Card(
        colors = MyFarmerTheme.cardColors.primary,
        shape = RoundedCornerShape(MyFarmerTheme.paddings.medium * 1.5f),
    ) {
        Column(
            modifier = Modifier.padding(MyFarmerTheme.paddings.medium),
            verticalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.medium)
        ) {

            Card(colors = MyFarmerTheme.cardColors.onPrimary) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MyFarmerTheme.paddings.medium),
                    text = "Product menu",
                    style = MyFarmerTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }

            ProductMenuList(
                menu = menu,
                onEdit = { itemToEdit ->
                    editMenuItemDialog = itemToEdit
                },
                onDelete = { itemToDelete ->
                    val newItems = menu.items - itemToDelete
                    onUpdateMenu(menu.copy(newItems))
                }
            )

            Button(onClick = { showAddMenuItemDialog = true }) {
                Text("Add product")
            }
        }
    }

    if (showAddMenuItemDialog) {
        AddMenuItemDialog(
            onAdd = { newItem ->
                val newMenu = menu.copy(menu.items + newItem)
                onUpdateMenu(newMenu)
                showAddMenuItemDialog = false
            },
            onDismiss = { showAddMenuItemDialog = false }
        )
    }

    editMenuItemDialog?.let { itemToEdit ->
        EditMenuItemDialog(
            menuItem = itemToEdit,
            onUpdate = { updatedItem ->
                val newItems = menu.items - itemToEdit + updatedItem
                onUpdateMenu(menu.copy(newItems))
                editMenuItemDialog = null
            },
            onDismiss = { editMenuItemDialog = null }
        )
    }
}

