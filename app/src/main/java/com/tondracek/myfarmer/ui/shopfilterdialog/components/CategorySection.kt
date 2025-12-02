package com.tondracek.myfarmer.ui.shopfilterdialog.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.shopcategory.domain.model.CategoryPopularity
import com.tondracek.myfarmer.ui.common.category.CategoryNameInput
import com.tondracek.myfarmer.ui.common.category.CategoryNameInputState
import com.tondracek.myfarmer.ui.common.divider.CustomHorizontalDivider
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
internal fun CategorySection(
    modifier: Modifier = Modifier,

    categoryNameInputState: CategoryNameInputState,
    onCategoryFilterInputChange: (String) -> Unit,

    selectedCategories: List<String>,
    onSelectedCategoriesAdd: (String) -> Unit,
    onSelectedCategoriesRemove: (String) -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = when (selectedCategories.isEmpty()) {
                true -> "Filtered categories"
                false -> "No filtered categories"
            }
        )
        SelectedCategoriesList(
            selectedCategories = selectedCategories,
            onCategoryRemoveClick = { categoryName ->
                onSelectedCategoriesRemove(categoryName)
            }
        )

        CustomHorizontalDivider()

        Text("Add a category to filters")
        CategoryNameInput(
            state = categoryNameInputState,
            onNameChanged = onCategoryFilterInputChange,
            onSuggestionClicked = { onSelectedCategoriesAdd(it) }
        )
        Button(onClick = {
            onSelectedCategoriesAdd(categoryNameInputState.categoryName)
        }) {
            Text(
                text = "Add category to filters",
                style = MyFarmerTheme.typography.textMedium,
            )
        }
    }
}

@Composable
private fun SelectedCategoriesList(
    modifier: Modifier = Modifier,
    selectedCategories: List<String>,
    onCategoryRemoveClick: (String) -> Unit,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.small)
    ) {
        items(selectedCategories) { categoryName: String ->
            Card(
                colors = MyFarmerTheme.cardColors.primary,
                shape = CircleShape,
            ) {
                Row(
                    modifier = Modifier.padding(8.dp, 0.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = categoryName)
                    Icon(
                        modifier = Modifier.clickable { onCategoryRemoveClick(categoryName) },
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove category",
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CategorySectionPreview() {
    val selectedCategories = listOf("Fruits", "Vegetables", "Dairy")
    val availableCategories = listOf(
        CategoryPopularity("Fruits", 150),
        CategoryPopularity("Vegetables", 120),
        CategoryPopularity("Dairy", 100),
        CategoryPopularity("Bakery", 80),
        CategoryPopularity("Meat", 70)
    )

    MyFarmerPreview {
        CategorySection(
            modifier = Modifier,
            categoryNameInputState = CategoryNameInputState(
                categoryName = "Aa",
                suggestions = availableCategories
            ),
            onCategoryFilterInputChange = {},
            selectedCategories = selectedCategories,
            onSelectedCategoriesAdd = {},
            onSelectedCategoriesRemove = {}
        )
    }

}