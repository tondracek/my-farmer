package com.example.myfarmer.feature.shopslistview.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myfarmer.feature.shopslistview.presentation.model.ShopListViewItem
import com.example.myfarmer.feature.shopslistview.presentation.model.toListItem
import com.example.myfarmer.feature.shopsmapview.presentation.components.ImageView
import com.example.myfarmer.shared.domain.model.ShopId
import com.example.myfarmer.shared.domain.model.sampleShops
import com.example.myfarmer.shared.location.km
import com.example.myfarmer.shared.theme.MyFarmerTheme
import com.example.myfarmer.shared.ui.components.CategoriesRow
import com.example.myfarmer.shared.ui.preview.AsyncImagePreviewFix
import com.example.myfarmer.shared.ui.preview.PreviewApi34

@Composable
fun ShopListItemCard(
    modifier: Modifier = Modifier,
    shop: ShopListViewItem,
    onNavigateToShopDetail: (ShopId) -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = { onNavigateToShopDetail(shop.id) },
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ImageView(
                    modifier = Modifier
                        .sizeIn(maxHeight = 120.dp, maxWidth = 120.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    imageResource = shop.picture
                )
                Column {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = shop.name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleMedium
                        )

                        if (shop.distance != null)
                            Text(
                                text = shop.distance.toString(),
                                style = MaterialTheme.typography.labelMedium
                            )
                    }
                    Text(
                        text = shop.description,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            CategoriesRow(shop.categories)
        }
    }
}

@PreviewApi34
@Composable
private fun ShopListItemCardPreview() {
    MyFarmerTheme {
        AsyncImagePreviewFix {
            ShopListItemCard(
                shop = sampleShops.first().toListItem(3.5.km),
                onNavigateToShopDetail = {}
            )
        }
    }
}