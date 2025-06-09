package com.example.myfarmer.feature.shopscreen.presentation.root

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ShopScreenViewModel @Inject constructor(
) : ViewModel() {

    private val _state = MutableStateFlow(ShopsScreenState())
    val state: StateFlow<ShopsScreenState> = _state

    fun onViewModeSelected(shopsViewMode: ShopsViewMode) = _state.update {
        it.copy(viewMode = shopsViewMode)
    }
}