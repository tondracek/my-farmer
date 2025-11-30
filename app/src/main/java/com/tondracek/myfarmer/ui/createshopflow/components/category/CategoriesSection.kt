package com.tondracek.myfarmer.ui.createshopflow.components.category

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
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun CategoriesSection(
    categories: List<ShopCategory>,
    onUpdateCategories: (List<ShopCategory>) -> Unit,
) {
    var showAddCategoryDialog by remember { mutableStateOf(false) }

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
                    text = "Product categories",
                    style = MyFarmerTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }

            FlowCategoryChips(
                categories = categories,
                onRemove = { categoryToRemove ->
                    val newCategories = categories - categoryToRemove
                    onUpdateCategories(newCategories)
                }
            )

            Button(onClick = { showAddCategoryDialog = true }) {
                Text("Add category")
            }
        }
    }

    if (showAddCategoryDialog) {
        AddCategoryDialog(
            onAdd = {
                val newCategories = categories + it
                onUpdateCategories(newCategories)
                showAddCategoryDialog = false
            },
            onDismiss = { showAddCategoryDialog = false }
        )
    }
}
