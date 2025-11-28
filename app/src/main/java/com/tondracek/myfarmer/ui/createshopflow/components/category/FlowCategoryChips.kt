package com.tondracek.myfarmer.ui.createshopflow.components.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import com.tondracek.myfarmer.ui.common.color.contrastColor

@Composable
fun FlowCategoryChips(
    categories: List<ShopCategory>,
    onRemove: (ShopCategory) -> Unit
) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        categories.forEach { category ->
            CategoryChip(category = category, onRemove = { onRemove(category) })
        }
    }
}

@Composable
private fun CategoryChip(
    category: ShopCategory,
    onRemove: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = category.color,
            contentColor = contrastColor(category.color)
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp, 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = category.name)
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove category",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.clickable { onRemove() }
            )
        }
    }
}
