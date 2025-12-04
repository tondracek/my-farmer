package com.tondracek.myfarmer.ui.shopfilterdialog

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.core.usecaseresult.getOrElse
import com.tondracek.myfarmer.location.model.Distance
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.shopcategory.domain.model.CategoryPopularity
import com.tondracek.myfarmer.shopcategory.domain.usecase.GetCategorySuggestionsUC
import com.tondracek.myfarmer.shopcategory.domain.usecase.GetMostPopularCategoriesUC
import com.tondracek.myfarmer.shopfilters.data.ShopFilterRepositoryFactory
import com.tondracek.myfarmer.shopfilters.domain.model.ShopFilters
import com.tondracek.myfarmer.ui.common.category.CategoryNameInputState
import com.tondracek.myfarmer.ui.core.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.SortedSet
import javax.inject.Inject

@HiltViewModel
class ShopFilterDialogViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    filterRepositoryFactory: ShopFilterRepositoryFactory,
    getMostPopularCategoriesUC: GetMostPopularCategoriesUC,
    getCategorySuggestionsUC: GetCategorySuggestionsUC,
    private val appNavigator: AppNavigator,
) : ViewModel() {

    val filterRepositoryKey = savedStateHandle.getShopFilterRepositoryKey()
    val filterRepository = filterRepositoryFactory.createOrGet(filterRepositoryKey)
    private val filters: MutableStateFlow<ShopFilters> = MutableStateFlow(ShopFilters.None)

    private val categoryFilterInput: MutableStateFlow<String> = MutableStateFlow("")
    private val mostPopularCategories: Flow<List<CategoryPopularity>> =
        getMostPopularCategoriesUC()
            .map { it.getOrElse(emptyList()) }


    private val categoryNameInputState: Flow<CategoryNameInputState> = combine(
        categoryFilterInput,
        mostPopularCategories,
    ) { categoryFilterInput, mostPopularCategories ->
        val suggestions = getCategorySuggestionsUC(categoryFilterInput, mostPopularCategories)
        CategoryNameInputState(
            categoryName = categoryFilterInput,
            suggestions = suggestions,
        )
    }

    val state: StateFlow<ShopFilterDialogState> = combine(
        filters,
        categoryNameInputState,
    ) { currentFilters, categoryNameInputState ->
        ShopFilterDialogState(
            filters = currentFilters,
            categoryNameInputState = categoryNameInputState,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = ShopFilterDialogState.Initial
    )

    init {
        viewModelScope.launch {
            val initialFilters = filterRepository.filters.value
            filters.update { initialFilters }
        }
    }

    fun onCategoryFilterInputChange(newInput: String) = categoryFilterInput.update {
        newInput
    }

    fun onSelectedCategoriesAdd(category: String) = filters.update {
        if (category.isBlank()) return@update it

        val newCategories: SortedSet<String> = sortedSetOf(*it.categories.toTypedArray())
        newCategories.add(category)
        it.copy(categories = newCategories)
    }.also {
        onCategoryFilterInputChange("")
    }

    fun onSelectedCategoriesRemove(category: String) = filters.update {
        val newCategories: SortedSet<String> = sortedSetOf(*it.categories.toTypedArray())
        newCategories.remove(category)
        it.copy(categories = newCategories)
    }

    fun onMaxDistanceChange(newMaxDistanceKm: Distance?) = filters.update {
        it.copy(maxDistanceKm = newMaxDistanceKm)
    }

    fun onMinRatingChange(newMinRating: Rating) = filters.update {
        it.copy(minRating = newMinRating)
    }

    fun onApplyFiltersClick() = viewModelScope.launch {
        filterRepository.updateFilters(filters.value)
        appNavigator.navigateBack()
    }

    fun onCancelClick() = appNavigator.navigateBack()
}