package com.tondracek.myfarmer.ui.createshopflow.components.productmenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.productmenu.domain.model.MenuItem
import com.tondracek.myfarmer.productmenu.domain.model.PriceLabel
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview

@Composable
fun AddMenuItemDialog(
    onAdd: (MenuItem) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var inStock by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.add_product)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.product_name)) })
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(R.string.description)) })
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text(stringResource(R.string.price)) },
                    supportingText = { Text(stringResource(R.string.price_label_input_hint)) },
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.is_in_stock))
                    Switch(checked = inStock, onCheckedChange = { inStock = it })
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onAdd(
                    MenuItem(
                        name = name,
                        description = description,
                        price = PriceLabel(price),
                        inStock = inStock,
                    )
                )
            }) { Text(stringResource(R.string.add)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    )
}

@Composable
fun EditMenuItemDialog(
    menuItem: MenuItem,
    onUpdate: (MenuItem) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(menuItem.name) }
    var description by remember { mutableStateOf(menuItem.description) }
    var price by remember { mutableStateOf(menuItem.price.value) }
    var inStock by remember { mutableStateOf(menuItem.inStock) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.edit_product)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    name,
                    { name = it },
                    label = { Text(stringResource(R.string.product_name)) })
                OutlinedTextField(
                    description,
                    { description = it },
                    label = { Text(stringResource(R.string.description)) })
                OutlinedTextField(
                    price,
                    { price = it },
                    label = { Text(stringResource(R.string.price)) })
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.is_in_stock))
                    Switch(checked = inStock, onCheckedChange = { inStock = it })
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onUpdate(
                    menuItem.copy(
                        name = name,
                        description = description,
                        price = PriceLabel(price),
                        inStock = inStock,
                    )
                )
            }) { Text(stringResource(R.string.update)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    )
}

@Preview
@Composable
private fun AddMenuItemDialogPreview() {
    MyFarmerPreview {
        AddMenuItemDialog(
            onAdd = {},
            onDismiss = {}
        )
    }
}