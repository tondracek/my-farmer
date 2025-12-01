package com.tondracek.myfarmer.ui.common.category

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun CategoryLabel(category: ShopCategory) {
    Card(
        colors = MyFarmerTheme.cardColors.custom(category.color),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Text(
            modifier = Modifier.padding(8.dp, 0.dp),
            text = category.name
        )
    }
}


@Preview
@Composable
fun CategoryLabelPrev() {
    CategoryLabel(category = ShopCategory("Vegetables", Color.Blue))
}

@Preview
@Composable
fun CategoryLabelPrev2() {
    CategoryLabel(category = ShopCategory("Fruits", Color.White))
}