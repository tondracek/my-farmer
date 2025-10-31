package com.tondracek.myfarmer.ui.common.navbar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.auth.domain.usecase.IsLoggedInUC
import com.tondracek.myfarmer.core.usecaseresult.getOrElse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NavBarViewModel @Inject constructor(
    isLoggedInUC: IsLoggedInUC,
) : ViewModel() {

    val selectedItem: MutableStateFlow<Int> = MutableStateFlow(1)

    val state: StateFlow<NavBarState> = combine(
        selectedItem,
        isLoggedInUC(),
    ) { selectedItem, loggedInResult ->
        val loggedIn = loggedInResult.getOrElse(false)

        NavBarState(
            selectedItem = selectedItem,
            loggedIn = loggedIn,
        )
    }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.Lazily,
        initialValue = NavBarState(
            selectedItem = selectedItem.value,
            loggedIn = false,
        ),
    )

    fun onItemSelected(index: Int) {
        selectedItem.value = index
    }
}