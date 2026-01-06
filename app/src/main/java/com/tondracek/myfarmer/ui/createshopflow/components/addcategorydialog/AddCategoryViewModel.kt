package com.tondracek.myfarmer.ui.createshopflow.components.addcategorydialog

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.core.usecaseresult.getOrElse
import com.tondracek.myfarmer.shopcategory.domain.model.CategoryPopularity
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import com.tondracek.myfarmer.shopcategory.domain.usecase.GetCategorySuggestionsUC
import com.tondracek.myfarmer.shopcategory.domain.usecase.GetMostPopularCategoriesUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

const val NEW_CATEGORY_DIALOG_VALUE = "new_category"

@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    getMostPopularCategories: GetMostPopularCategoriesUC,
    getCategorySuggestions: GetCategorySuggestionsUC,
) : ViewModel() {

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

    fun onAddCategory(category: ShopCategory) = viewModelScope.launch {
        _effects.emit(AddCategoryEffect.OnAddCategory(category))
    }

    fun onDismissRequest() = viewModelScope.launch {
        _effects.emit(AddCategoryEffect.OnGoBackClicked)
    }

    private val _effects = MutableSharedFlow<AddCategoryEffect>(extraBufferCapacity = 1)
    val effects: SharedFlow<AddCategoryEffect> = _effects
}

sealed interface AddCategoryEffect {

    data object OnGoBackClicked : AddCategoryEffect

    data class OnAddCategory(val category: ShopCategory) : AddCategoryEffect
}