package com.tondracek.myfarmer.ui.createshopflow.common.components.addcategorydialog

import androidx.compose.ui.graphics.Color
import com.tondracek.myfarmer.shopcategory.domain.model.CategoryPopularity

data class AddCategoryDialogState(
    val categoryName: String,
    val selectedColor: Color,
    val suggestions: List<CategoryPopularity>,
)