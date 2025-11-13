package com.tondracek.myfarmer.ui.common.layout.shopdetaillayout.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.productmenu.domain.model.MenuItem
import com.tondracek.myfarmer.productmenu.domain.model.ProductMenu
import com.tondracek.myfarmer.shop.data.shop1
import com.tondracek.myfarmer.ui.core.preview.PreviewDark
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun MenuSection(
    modifier: Modifier = Modifier,
    menu: ProductMenu,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MyFarmerTheme.colors.secondaryContainer,
            contentColor = MyFarmerTheme.colors.onSecondaryContainer,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MyFarmerTheme.paddings.large),
            verticalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MyFarmerTheme.colors.primaryContainer,
                    contentColor = MyFarmerTheme.colors.onPrimaryContainer,
                )
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MyFarmerTheme.paddings.medium),
                    text = stringResource(R.string.product_menu),
                    style = MyFarmerTheme.typography.textLarge,
                    textAlign = TextAlign.Center
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = MyFarmerTheme.paddings.large),
                color = MyFarmerTheme.colors.onSecondaryContainer
            )
            when (menu.items.isEmpty()) {
                true ->
                    Text(text = stringResource(R.string.no_products))


                false -> menu.items.forEach { menuItem ->
                    ProductMenuItemCard(item = menuItem)
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = MyFarmerTheme.paddings.extraLarge),
                        color = MyFarmerTheme.colors.onSecondaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductMenuItemCard(
    modifier: Modifier = Modifier,
    item: MenuItem,
) {
    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = item.name,
                style = MyFarmerTheme.typography.headerSmall,
            )
            Column(horizontalAlignment = AbsoluteAlignment.Right) {
                Text(
                    modifier = Modifier.padding(start = MyFarmerTheme.paddings.medium),
                    text = item.price.toString(),
                    style = MyFarmerTheme.typography.textMedium,
                )
                InStockLabel(isInStock = item.inStock)
            }
        }
        if (!item.description.isNullOrBlank()) {
            Text(
                text = item.description,
            )
        }
    }
}

@Composable
private fun InStockLabel(
    modifier: Modifier = Modifier,
    isInStock: Boolean
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = when (isInStock) {
                true -> stringResource(R.string.in_stock)
                false -> stringResource(R.string.out_of_stock)
            },
            style = MyFarmerTheme.typography.textSmall,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = when (isInStock) {
                true -> Icons.Filled.CheckCircle
                false -> Icons.Filled.RemoveCircleOutline
            },
            tint = when (isInStock) {
                true -> MyFarmerTheme.colors.success
                false -> MyFarmerTheme.colors.error
            },
            contentDescription = when (isInStock) {
                true -> stringResource(R.string.in_stock)
                false -> stringResource(R.string.out_of_stock)
            },
        )
    }
}

@Preview
@Composable
private fun MenuSectionPrev() {
    MyFarmerTheme {
        MenuSection(
            menu = shop1.menu
        )
    }
}

@PreviewDark
@Composable
private fun MenuSectionPrev0() {
    MyFarmerTheme {
        MenuSection(
            menu = shop1.menu
        )
    }
}

@PreviewDark
@Composable
private fun MenuSectionPrevEmpty() {
    MyFarmerTheme {
        MenuSection(
            menu = ProductMenu(emptyList())
        )
    }
}

