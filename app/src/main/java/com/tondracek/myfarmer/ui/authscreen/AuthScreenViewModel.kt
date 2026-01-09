package com.tondracek.myfarmer.ui.authscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthScreenViewModel @Inject constructor() : ViewModel() {

    private val _effects = MutableSharedFlow<AuthScreenEffect>(extraBufferCapacity = 1)
    val effects: SharedFlow<AuthScreenEffect> = _effects.asSharedFlow()

    fun navigateToProfileScreen() = viewModelScope.launch {
        _effects.emit(AuthScreenEffect.NavigateToProfileScreen)
    }

    fun showError(message: String) = viewModelScope.launch {
        _effects.emit(AuthScreenEffect.ShowError(message))
    }
}

sealed interface AuthScreenEffect {
    object NavigateToProfileScreen : AuthScreenEffect
    data class ShowError(val message: String) : AuthScreenEffect
}