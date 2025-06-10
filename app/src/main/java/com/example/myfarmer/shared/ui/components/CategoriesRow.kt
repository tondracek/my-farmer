package com.example.myfarmer.shared.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myfarmer.shared.domain.model.Category
import com.example.myfarmer.shared.domain.model.sampleShops
import com.example.myfarmer.shared.theme.MyFarmerTheme

@Composable
fun CategoriesRow(
    categories: List<Category>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        categories.forEach { CategoryLabel(category = it) }
    }
}

@Preview
@Composable
fun CategoriesRowPreview() {
    MyFarmerTheme {
        CategoriesRow(sampleShops.first().categories)
    }
}