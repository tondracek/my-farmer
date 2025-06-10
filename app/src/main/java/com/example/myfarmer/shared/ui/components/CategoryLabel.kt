package com.example.myfarmer.shared.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myfarmer.shared.domain.model.Category

@Composable
fun CategoryLabel(category: Category) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = category.color,
            contentColor = contrastColor(category.color)
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Text(
            modifier = Modifier.padding(8.dp, 0.dp),
            text = category.name
        )
    }
}

private fun contrastColor(color: Color): Color {
    val luminance = (0.299 * color.red + 0.587 * color.green + 0.114 * color.blue)
    return if (luminance > 0.5) Color.Black else Color.White
}

@Preview
@Composable
fun CategoryLabelPrev() {
    CategoryLabel(category = Category("Vegetables", Color.Blue))
}

@Preview
@Composable
fun CategoryLabelPrev2() {
    CategoryLabel(category = Category("Fruits", Color.White))
}