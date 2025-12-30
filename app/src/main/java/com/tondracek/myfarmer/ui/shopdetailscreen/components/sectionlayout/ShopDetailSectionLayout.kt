package com.tondracek.myfarmer.ui.shopdetailscreen.components.sectionlayout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

class ShopDetailSectionScope {
    private val items = mutableListOf<@Composable () -> Unit>()

    fun item(content: @Composable () -> Unit) {
        items += content
    }

    fun <T> item(input: T, transform: @Composable (T) -> Unit) {
        items += { transform(input) }
    }

    fun <T> itemNotNull(input: T?, transform: @Composable (T) -> Unit) {
        if (input == null) return
        items += { transform(input) }
    }

    fun <T> items(inputs: Iterable<T>, transform: @Composable (T) -> Unit) {
        items += inputs.map { item -> { transform(item) } }
    }

    @Composable
    internal fun Render(content: @Composable (List<@Composable () -> Unit>) -> Unit) {
        content(items)
    }
}

@Composable
fun ShopDetailSectionLayout(
    modifier: Modifier = Modifier,
    title: String,
    content: ShopDetailSectionScope.() -> Unit,
) {
    val scope = remember { ShopDetailSectionScope() }

    ShopDetailSectionCustomLayout(
        modifier = modifier,
        title = title,
    ) {
        scope.content()
        scope.Render { items ->
            items.forEachIndexed { index, item ->
                item()
                if (index < items.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = MyFarmerTheme.paddings.small),
                        color = LocalContentColor.current
                    )
                }
            }
        }
    }
}

@Composable
fun ShopDetailSectionCustomLayout(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = MyFarmerTheme.cardColors.secondary,
    ) {
        Column(modifier = Modifier.padding(horizontal = MyFarmerTheme.paddings.small)) {
            Card(
                modifier = Modifier.padding(top = MyFarmerTheme.paddings.small),
                colors = MyFarmerTheme.cardColors.primary
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MyFarmerTheme.paddings.medium),
                    text = title,
                    style = MyFarmerTheme.typography.textLarge,
                    textAlign = TextAlign.Center
                )
            }
            content()
        }
    }
}

@Preview
@Composable
private fun ShopDetailSectionListPreview() {
    MyFarmerTheme {
        ShopDetailSectionLayout(
            title = "Section Title",
        ) {
            items(listOf(1, 2, 3)) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MyFarmerTheme.paddings.medium),
                    text = "Item ${it + 1}",
                    style = MyFarmerTheme.typography.textMedium,
                )
            }
        }
    }
}