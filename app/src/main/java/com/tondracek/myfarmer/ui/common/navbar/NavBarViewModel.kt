package com.tondracek.myfarmer.ui.common.navbar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.auth.domain.usecase.IsLoggedInUC
import com.tondracek.myfarmer.core.usecaseresult.getOrElse
import com.tondracek.myfarmer.ui.core.navigation.AppNavigator
import com.tondracek.myfarmer.ui.core.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NavBarViewModel @Inject constructor(
    isLoggedInUC: IsLoggedInUC,
    private val navigator: AppNavigator,
) : ViewModel() {

    val currentRoute: Flow<Route?> = navigator.getCurrentRoute()

    val state: StateFlow<NavBarState> = combine(
        currentRoute,
        isLoggedInUC(),
    ) { currentRoute, loggedInResult ->
        val loggedIn = loggedInResult.getOrElse(false)

        NavBarState(
            currentRoute = currentRoute,
            loggedIn = loggedIn,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = NavBarState(
            currentRoute = null,
            loggedIn = false,
        ),
    )

    fun onNavigate(route: Route) = navigator.navigate(route)
}