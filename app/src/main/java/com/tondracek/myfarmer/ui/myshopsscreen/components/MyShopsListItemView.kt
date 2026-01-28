package com.tondracek.myfarmer.ui.myshopsscreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.ui.common.category.CategoriesRow
import com.tondracek.myfarmer.ui.common.image.ImageView
import com.tondracek.myfarmer.ui.common.sample.shop0
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.myshopsscreen.MyShopsListItem
import com.tondracek.myfarmer.ui.myshopsscreen.toMyShopsListItem

@Composable
fun MyShopsListItemView(
    modifier: Modifier = Modifier,
    shop: MyShopsListItem,
    onShopClick: (shopId: ShopId) -> Unit,
    onUpdateShopClick: (shopId: ShopId) -> Unit,
    onDeleteShopClick: (shopId: ShopId) -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = { onShopClick(shop.id) }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(Modifier.sizeIn(maxHeight = 120.dp, maxWidth = 120.dp)) {
                    ImageView(
                        modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                        imageResource = shop.image,
                        openable = false,
                    )
                }
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
                    CategoriesRow(shop.categories)
                }
            }

            Box(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Button(
                        onClick = { onUpdateShopClick(shop.id) },
                        colors = MyFarmerTheme.buttonColors.tertiary
                    ) {
                        Text(text = stringResource(R.string.update_shop))
                    }
                    Button(
                        onClick = { onDeleteShopClick(shop.id) },
                        colors = MyFarmerTheme.buttonColors.error
                    ) {
                        Text(text = stringResource(R.string.delete_shop))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun MyShopsListItemViewPreview() {
    MyFarmerPreview(useSurface = false) {
        MyShopsListItemView(
            shop = shop0.toMyShopsListItem(Rating.ZERO),
            onShopClick = {},
            onUpdateShopClick = {},
            onDeleteShopClick = {}
        )
    }
}