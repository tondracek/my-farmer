package com.example.myfarmer.feature.shopscreen.presentation.mapview.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myfarmer.shared.domain.Category

@Composable
fun CategoryLabel(category: Category) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = category.color,
            contentColor = MaterialTheme.colorScheme.contentColorFor(category.color)
        ),
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
    CategoryLabel(category = Category("Vegetables", Color.Green))
}