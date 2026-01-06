package com.tondracek.myfarmer.ui.mainshopscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.ui.common.shop.filter.ShopFiltersRepositoryKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainShopsScreenViewModel @Inject constructor() : ViewModel() {

    private val _events = MutableSharedFlow<MainShopsScreenEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<MainShopsScreenEvent> = _events

    fun onOpenFiltersDialog() = viewModelScope.launch {
        val filtersKey = ShopFiltersRepositoryKeys.MAIN_SHOPS_SCREEN
        _events.emit(MainShopsScreenEvent.OpenFiltersDialog(filtersKey))
    }
}

sealed interface MainShopsScreenEvent {

    data class OpenFiltersDialog(val filtersKey: String) : MainShopsScreenEvent
}