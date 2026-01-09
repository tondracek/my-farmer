package com.tondracek.myfarmer.ui.common.productmenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun InStockLabel(
    modifier: Modifier = Modifier,
    inStock: Boolean
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = when (inStock) {
                true -> stringResource(R.string.in_stock)
                false -> stringResource(R.string.out_of_stock)
            },
            style = MyFarmerTheme.typography.textSmall,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = when (inStock) {
                true -> Icons.Filled.CheckCircle
                false -> Icons.Filled.RemoveCircleOutline
            },
            tint = when (inStock) {
                true -> MyFarmerTheme.colors.success
                false -> MyFarmerTheme.colors.error
            },
            contentDescription = when (inStock) {
                true -> stringResource(R.string.in_stock)
                false -> stringResource(R.string.out_of_stock)
            },
        )
    }
}