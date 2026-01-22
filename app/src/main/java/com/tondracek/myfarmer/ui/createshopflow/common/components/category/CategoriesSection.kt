package com.tondracek.myfarmer.ui.createshopflow.common.components.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun CategoriesSection(
    categories: List<ShopCategory>,
    onOpenAddCategoryDialog: () -> Unit,
    onDeleteCategory: (ShopCategory) -> Unit,
) {
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
                    text = stringResource(R.string.product_categories),
                    style = MyFarmerTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }

            FlowCategoryChips(
                categories = categories,
                onRemove = onDeleteCategory,
            )

            Button(onClick = onOpenAddCategoryDialog) {
                Text(stringResource(R.string.add_category))
            }
        }
    }
}
