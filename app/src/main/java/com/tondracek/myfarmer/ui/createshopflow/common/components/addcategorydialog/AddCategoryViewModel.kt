package com.tondracek.myfarmer.ui.createshopflow.common.components.addcategorydialog

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.core.domain.domainresult.getOrElse
import com.tondracek.myfarmer.shopcategory.domain.model.CategoryPopularity
import com.tondracek.myfarmer.shopcategory.domain.usecase.GetCategorySuggestionsUC
import com.tondracek.myfarmer.shopcategory.domain.usecase.GetMostPopularCategoriesUC
import com.tondracek.myfarmer.ui.core.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    getMostPopularCategories: GetMostPopularCategoriesUC,
    getCategorySuggestions: GetCategorySuggestionsUC,
) : BaseViewModel<Nothing>() {

    private val mostPopularCategories: Flow<List<CategoryPopularity>> =
        getMostPopularCategories().getOrElse(emptyList())

    private val inputCategoryName: MutableStateFlow<String> =
        MutableStateFlow("")

    private val selectedColor: MutableStateFlow<Color> =
        MutableStateFlow(Color.White)

    private val suggestions: StateFlow<List<CategoryPopularity>> = combine(
        mostPopularCategories,
        inputCategoryName
    ) { popular, input ->
        getCategorySuggestions(input, popular)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        emptyList()
    )

    val state: StateFlow<AddCategoryDialogState> = combine(
        suggestions,
        inputCategoryName,
        selectedColor
    ) { suggestions, categoryName, selectedColor ->
        AddCategoryDialogState(
            categoryName = categoryName,
            selectedColor = selectedColor,
            suggestions = suggestions,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AddCategoryDialogState(
            categoryName = "",
            selectedColor = Color.White,
            suggestions = emptyList()
        )
    )

    fun onCategoryNameChange(newName: String) =
        inputCategoryName.update { newName }

    fun onColorSelected(newColor: Color) =
        selectedColor.update { newColor }
}
