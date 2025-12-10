package com.tondracek.myfarmer.ui.shopfilterdialog

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
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
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.components.filterChipColors
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
            var openedSection by remember { mutableStateOf(ShopFilterSection.NONE) }

            Content(
                state = state,
                openedSection = openedSection,
                onOpenedSectionClick = { section ->
                    openedSection =
                        if (openedSection == section) ShopFilterSection.NONE else section
                },
                onCategoryFilterInputChange = onCategoryFilterInputChange,
                onSelectedCategoriesAdd = onSelectedCategoriesAdd,
                onSelectedCategoriesRemove = onSelectedCategoriesRemove,
                onMaxDistanceChange = onMaxDistanceChange,
                onMinRatingChange = onMinRatingChange
            )
        },
        confirmButton = {
            Button(onClick = onApplyFiltersClick) { Text("Apply filters") }
        },
        dismissButton = {
            TextButton(onClick = onCancelClick) { Text("Cancel") }
        }
    )
}

private enum class ShopFilterSection {
    CATEGORIES,
    DISTANCE,
    RATING,
    NONE
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    state: ShopFilterDialogState,
    openedSection: ShopFilterSection,
    onOpenedSectionClick: (ShopFilterSection) -> Unit,
    onCategoryFilterInputChange: (String) -> Unit,
    onSelectedCategoriesAdd: (String) -> Unit,
    onSelectedCategoriesRemove: (String) -> Unit,
    onMaxDistanceChange: (Distance?) -> Unit,
    onMinRatingChange: (Rating) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ChipRow(
            state = state,
            openedSection = openedSection,
            onOpenedSectionClick = onOpenedSectionClick
        )

        when (openedSection) {
            ShopFilterSection.CATEGORIES -> CategorySection(
                onCategoryFilterInputChange = onCategoryFilterInputChange,
                selectedCategories = state.filters.categories.toList(),
                categoryNameInputState = state.categoryNameInputState,
                onSelectedCategoriesAdd = onSelectedCategoriesAdd,
                onSelectedCategoriesRemove = onSelectedCategoriesRemove,
            )

            ShopFilterSection.DISTANCE -> DistanceSection(
                selectedDistance = state.filters.maxDistanceKm,
                onMaxDistanceChange = onMaxDistanceChange,
            )

            ShopFilterSection.RATING -> RatingSection(
                selectedMinRating = state.filters.minRating,
                onRatingChange = onMinRatingChange,
            )

            ShopFilterSection.NONE -> {
                Text(
                    text = stringResource(R.string.select_filter_above_to_edit_it),
                    style = MyFarmerTheme.typography.titleSmall,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun ChipRow(
    state: ShopFilterDialogState,
    openedSection: ShopFilterSection,
    onOpenedSectionClick: (ShopFilterSection) -> Unit
) {
    @Composable
    fun Chip(value: ShopFilterSection, content: @Composable (() -> Unit)) {
        FilterChip(
            selected = openedSection == value,
            onClick = { onOpenedSectionClick(value) },
            label = content,
            shape = CircleShape,
            colors = MyFarmerTheme.filterChipColors.primary,
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MyFarmerTheme.paddings.small)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.extraSmall)
    ) {
        Chip(ShopFilterSection.CATEGORIES) {
            val text = stringResource(
                R.string.category_filters,
                state.filters.categories.size,
            )
            Text(text)
        }
        Chip(ShopFilterSection.DISTANCE) {
            val text = when (state.filters.maxDistanceKm) {
                null -> stringResource(R.string.distance_filter_no_filter)
                else -> stringResource(
                    R.string.distance_filter,
                    state.filters.maxDistanceKm.toStringTranslated()
                )
            }
            Text(text)
        }
        Chip(ShopFilterSection.RATING) {
            val text = stringResource(
                R.string.minimum_rating_filter_stars,
                state.filters.minRating.stars
            )
            Text(text)
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
private fun ShopFilterDialogCategoryPreview() {
    MyFarmerPreview {
        Content(
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
            openedSection = ShopFilterSection.CATEGORIES,
            onOpenedSectionClick = {},
            onCategoryFilterInputChange = {},
            onSelectedCategoriesAdd = {},
            onSelectedCategoriesRemove = {},
            onMaxDistanceChange = {},
            onMinRatingChange = {},
        )
    }
}