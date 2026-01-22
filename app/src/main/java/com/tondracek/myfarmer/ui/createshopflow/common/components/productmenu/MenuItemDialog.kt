package com.tondracek.myfarmer.ui.createshopflow.common.components.productmenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.productmenu.domain.model.MenuItem
import com.tondracek.myfarmer.productmenu.domain.model.PriceLabel
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview

enum class MenuItemDialogMode { ADD, EDIT }

@Composable
fun MenuItemDialog(
    initial: MenuItem? = null,
    mode: MenuItemDialogMode,
    onConfirm: (MenuItem) -> Unit,
    onDismiss: () -> Unit,
) {
    var name by remember { mutableStateOf(initial?.name.orEmpty()) }
    var description by remember { mutableStateOf(initial?.description.orEmpty()) }
    var price by remember { mutableStateOf(initial?.price?.value.orEmpty()) }
    var inStock by remember { mutableStateOf(initial?.inStock ?: true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            val title = when (mode) {
                MenuItemDialogMode.ADD -> stringResource(R.string.add_product)
                MenuItemDialogMode.EDIT -> stringResource(R.string.edit_product)
            }
            Text(title)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.product_name)) },
                    singleLine = true,
                )
                OutlinedTextField(
                    modifier = Modifier.heightIn(max = 150.dp),
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(R.string.description)) },
                )
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text(stringResource(R.string.price)) },
                    supportingText = { Text(stringResource(R.string.price_label_input_hint)) },
                    singleLine = true,
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
                onConfirm(
                    MenuItem(
                        name = name,
                        description = description,
                        price = PriceLabel(price),
                        inStock = inStock,
                    )
                )
            }) {
                val onConfirmText = when (mode) {
                    MenuItemDialogMode.ADD -> stringResource(R.string.add)
                    MenuItemDialogMode.EDIT -> stringResource(R.string.update)
                }
                Text(onConfirmText)
            }
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
        MenuItemDialog(
            mode = MenuItemDialogMode.ADD,
            onConfirm = {},
            onDismiss = {},
        )
    }
}