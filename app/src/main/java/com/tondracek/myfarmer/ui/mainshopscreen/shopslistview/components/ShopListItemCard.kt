package com.tondracek.myfarmer.ui.mainshopscreen.shopslistview.components

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
import com.tondracek.myfarmer.location.domain.model.km
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.sample.shop0
import com.tondracek.myfarmer.ui.common.category.CategoriesRow
import com.tondracek.myfarmer.ui.common.distance.toStringTranslated
import com.tondracek.myfarmer.ui.common.image.ImageView
import com.tondracek.myfarmer.ui.common.rating.RatingStars
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.preview.PreviewApi34

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
                    imageResource = shop.image,
                    openable = false
                )
                Column {
                    if (!shop.name.isNullOrBlank())
                        Text(
                            text = shop.name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleMedium
                        )

                    if (!shop.description.isNullOrBlank())
                        Text(
                            text = shop.description,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyMedium
                        )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RatingStars(rating = shop.averageRating)

                        if (shop.distance != null)
                            Text(
                                text = shop.distance.toStringTranslated(),
                                style = MaterialTheme.typography.labelMedium
                            )
                    }
                }
            }
            CategoriesRow(shop.categories)
        }
    }
}

@PreviewApi34
@Composable
private fun ShopListItemCardPreview() {
    MyFarmerPreview {
        ShopListItemCard(
            shop = shop0.toListItem(3.5.km, Rating(3)),
            onNavigateToShopDetail = {}
        )
    }
}
