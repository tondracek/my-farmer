package com.tondracek.myfarmer.ui.core.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

open class BaseViewModel<Effect>() : ViewModel() {
    private val _effects = MutableSharedFlow<Effect>(extraBufferCapacity = 1)
    val effects: SharedFlow<Effect> = _effects.asSharedFlow()

    protected suspend fun emitEffect(effect: Effect) = _effects.emit(effect)
}