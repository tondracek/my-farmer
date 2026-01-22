package com.tondracek.myfarmer.ui.createshopflow.common.components.addcategorydialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory


@Composable
fun AddCategoryDialogComponent(
    isAddCategoryDialogOpen: Boolean,
    onAddCategoryDialogOpenChange: (Boolean) -> Unit,
    onAddCategory: (category: ShopCategory) -> Unit,
) {
    val addCategoryViewModel: AddCategoryViewModel = hiltViewModel()
    val addCategoryState by addCategoryViewModel.state.collectAsState()

    if (isAddCategoryDialogOpen) {
        AddCategoryDialog(
            state = addCategoryState,
            onCategoryNameChange = addCategoryViewModel::onCategoryNameChange,
            onColorSelected = addCategoryViewModel::onColorSelected,
            onAdd = { onAddCategory(it) },
            onDismiss = { onAddCategoryDialogOpenChange(false) }
        )
    }
}