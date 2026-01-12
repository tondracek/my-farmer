package com.tondracek.myfarmer.ui.common.navbar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.auth.domain.usecase.IsLoggedInUC
import com.tondracek.myfarmer.core.domain.usecaseresult.getOrElse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NavBarViewModel @Inject constructor(
    isLoggedInUC: IsLoggedInUC,
) : ViewModel() {

    val state: StateFlow<NavBarState> = isLoggedInUC()
        .getOrElse(false)
        .map { NavBarState(isLoggedIn = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = NavBarState(),
        )
}
