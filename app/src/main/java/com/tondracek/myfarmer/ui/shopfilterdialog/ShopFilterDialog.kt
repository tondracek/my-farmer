package com.tondracek.myfarmer.ui.shopfilterdialog

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.location.model.Distance
import com.tondracek.myfarmer.location.model.km
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.shopcategory.domain.model.CategoryPopularity
import com.tondracek.myfarmer.shopfilters.domain.model.ShopFilters
import com.tondracek.myfarmer.ui.common.category.CategoryNameInputState
import com.tondracek.myfarmer.ui.common.divider.CustomHorizontalDivider
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.shopfilterdialog.components.CategorySection
import com.tondracek.myfarmer.ui.shopfilterdialog.components.DistanceSection
import com.tondracek.myfarmer.ui.shopfilterdialog.components.RatingSection

@Composable
fun ShopFilterDialog(
    state: ShopFilterDialogState,

    onCategoryFilterInputChange: (String) -> Unit,

    onSelectedCategoriesAdd: (String) -> Unit,
    onSelectedCategoriesRemove: (String) -> Unit,
    onMaxDistanceChange: (Distance?) -> Unit,
    onMinRatingChange: (Rating) -> Unit,

    onApplyFiltersClick: () -> Unit,
    onCancelClick: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onCancelClick,
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.edit_shop_filters),
                textAlign = TextAlign.Center
            )
        },
        text = {
            var openedSection by remember { mutableStateOf<Int?>(null) }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                FilterSection(
                    hiddenText = stringResource(
                        R.string.category_filters,
                        state.filters.categories.size
                    ),
                    opened = openedSection == 1,
                    onClick = { openedSection = 1 }
                ) {
                    CategorySection(
                        onCategoryFilterInputChange = onCategoryFilterInputChange,
                        selectedCategories = state.filters.categories.toList(),
                        categoryNameInputState = state.categoryNameInputState,
                        onSelectedCategoriesAdd = onSelectedCategoriesAdd,
                        onSelectedCategoriesRemove = onSelectedCategoriesRemove,
                    )
                }

                CustomHorizontalDivider()

                FilterSection(
                    hiddenText = when (state.filters.maxDistanceKm) {
                        null -> stringResource(R.string.distance_filter_no_filter)
                        else -> stringResource(
                            R.string.distance_filter,
                            state.filters.maxDistanceKm
                        )
                    },
                    opened = openedSection == 2,
                    onClick = { openedSection = 2 }
                ) {
                    DistanceSection(
                        selectedDistance = state.filters.maxDistanceKm,
                        onMaxDistanceChange = onMaxDistanceChange,
                    )
                }

                CustomHorizontalDivider()

                FilterSection(
                    hiddenText = stringResource(
                        R.string.minimum_rating_filter_stars,
                        state.filters.minRating.stars
                    ),
                    opened = openedSection == 3,
                    onClick = { openedSection = 3 }
                ) {
                    RatingSection(
                        selectedMinRating = state.filters.minRating,
                        onRatingChange = onMinRatingChange,
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = onApplyFiltersClick) { Text("Apply filters") }
        },
        dismissButton = {
            TextButton(onClick = onCancelClick) { Text("Cancel") }
        }
    )
}

@Composable
private fun FilterSection(
    hiddenText: String,
    opened: Boolean,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.animateContentSize()) {
        when (opened) {
            true -> content()
            false -> Column(
                modifier = Modifier
                    .padding(vertical = MyFarmerTheme.paddings.small)
                    .clickable(onClick = onClick),
            ) {
                Text(
                    text = hiddenText,
                    style = MyFarmerTheme.typography.titleMedium
                )
                Text(
                    text = "Click to expand ›",
                    style = MyFarmerTheme.typography.textSmall
                )
            }
        }
    }
}

@Preview
@Composable
private fun ShopFilterDialogPreview() {
    MyFarmerPreview {
        ShopFilterDialog(
            state = ShopFilterDialogState.Initial,
            onCategoryFilterInputChange = {},
            onSelectedCategoriesAdd = {},
            onSelectedCategoriesRemove = {},
            onMaxDistanceChange = {},
            onMinRatingChange = {},
            onApplyFiltersClick = {},
            onCancelClick = {}
        )
    }
}

@Preview
@Composable
private fun ShopFilterDialogPreview1() {
    MyFarmerPreview {
        ShopFilterDialog(
            state = ShopFilterDialogState(
                filters = ShopFilters(
                    maxDistanceKm = 50.km,
                    minRating = Rating(4),
                    categories = sortedSetOf("Fruits", "Vegetables", "Dairy")
                ),
                categoryNameInputState = CategoryNameInputState(
                    categoryName = "Meat",
                    suggestions = listOf(
                        CategoryPopularity("Meat", 120),
                        CategoryPopularity("Bakery", 100),
                        CategoryPopularity("Beverages", 80),
                    )
                )
            ),
            onCategoryFilterInputChange = {},
            onSelectedCategoriesAdd = {},
            onSelectedCategoriesRemove = {},
            onMaxDistanceChange = {},
            onMinRatingChange = {},
            onApplyFiltersClick = {},
            onCancelClick = {}
        )
    }
}