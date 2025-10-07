package com.tondracek.myfarmer.ui.shopslistview.components

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
import com.tondracek.myfarmer.location.model.km
import com.tondracek.myfarmer.shop.data.sampleShops
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.ui.common.category.CategoriesRow
import com.tondracek.myfarmer.ui.common.image.ImageView
import com.tondracek.myfarmer.ui.core.preview.AsyncImagePreviewFix
import com.tondracek.myfarmer.ui.core.preview.PreviewApi34
import com.tondracek.myfarmer.ui.core.theme.MyFarmerTheme

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