package com.tondracek.myfarmer.ui.shopdetailscreen.components.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.productmenu.domain.model.MenuItem
import com.tondracek.myfarmer.productmenu.domain.model.ProductMenu
import com.tondracek.myfarmer.shop.sample.shop0
import com.tondracek.myfarmer.ui.common.productmenu.InStockLabel
import com.tondracek.myfarmer.ui.core.preview.PreviewDark
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.shopdetailscreen.components.sectionlayout.ShopDetailSectionLayout

@Composable
fun MenuSection(
    modifier: Modifier = Modifier,
    menu: ProductMenu,
) {
    ShopDetailSectionLayout(
        modifier = modifier,
        title = stringResource(R.string.product_menu),
    ) {
        when (menu.items.isEmpty()) {
            true -> item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MyFarmerTheme.paddings.small),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = stringResource(R.string.no_products))
                }
            }

            false -> items(menu.items) { menuItem -> ProductMenuItemCard(item = menuItem) }
        }
    }
}

@Composable
private fun ProductMenuItemCard(
    modifier: Modifier = Modifier,
    item: MenuItem,
) {
    Column(
        modifier = modifier.padding(MyFarmerTheme.paddings.medium),
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
                style = MyFarmerTheme.typography.titleMedium,
            )
            Column(horizontalAlignment = AbsoluteAlignment.Right) {
                Text(
                    modifier = Modifier.padding(start = MyFarmerTheme.paddings.medium),
                    text = item.price.toString(),
                    style = MyFarmerTheme.typography.textMedium,
                )
                InStockLabel(inStock = item.inStock)
            }
        }
        if (item.description.isNotBlank()) {
            Text(text = item.description)
        }
    }
}

@Preview
@Composable
private fun MenuSectionPrev() {
    MyFarmerTheme {
        MenuSection(
            menu = shop0.menu
        )
    }
}

@PreviewDark
@Composable
private fun MenuSectionPrev0() {
    MyFarmerTheme {
        MenuSection(
            menu = shop0.menu
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

