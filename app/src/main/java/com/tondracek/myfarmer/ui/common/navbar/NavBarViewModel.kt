package com.tondracek.myfarmer.ui.common.navbar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.auth.domain.usecase.IsLoggedInUC
import com.tondracek.myfarmer.core.usecaseresult.getOrElse
import com.tondracek.myfarmer.ui.core.navigation.AppNavigator
import com.tondracek.myfarmer.ui.core.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NavBarViewModel @Inject constructor(
    isLoggedInUC: IsLoggedInUC,
    private val navigator: AppNavigator,
) : ViewModel() {

    val selectedItem: MutableStateFlow<Int> = MutableStateFlow(0)

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

    fun onItemSelected(index: Int) = selectedItem.update { index }

    fun onNavigate(route: Route) = navigator.navigate(route)
}