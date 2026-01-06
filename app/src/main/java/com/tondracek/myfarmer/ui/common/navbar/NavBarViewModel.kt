package com.tondracek.myfarmer.ui.common.navbar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tondracek.myfarmer.auth.domain.usecase.IsLoggedInUC
import com.tondracek.myfarmer.core.usecaseresult.getOrElse
import com.tondracek.myfarmer.ui.core.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavBarViewModel @Inject constructor(
    isLoggedInUC: IsLoggedInUC,
) : ViewModel() {

    val isLoggedIn: StateFlow<Boolean> = isLoggedInUC().getOrElse(false)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = false,
        )

    private val _effects = MutableSharedFlow<NavBarEffect>(extraBufferCapacity = 1)
    val effects: SharedFlow<NavBarEffect> = _effects

    fun onNavigate(route: Route) = viewModelScope.launch {
        _effects.emit(NavBarEffect.Navigate(route))
    }
}

sealed interface NavBarEffect {

    data class Navigate(val route: Route) : NavBarEffect
}