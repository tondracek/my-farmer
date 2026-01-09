package com.tondracek.myfarmer.ui.core.appstate

import androidx.compose.runtime.compositionLocalOf
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

val LocalAppUiController = compositionLocalOf<AppUiController> {
    error("AppUiController not provided")
}

class AppUiController {

    private val _errors = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val errors = _errors.asSharedFlow()

    fun showErrorMessage(message: String) = _errors.tryEmit(message)

    private val _successMessages = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val successMessages = _successMessages.asSharedFlow()

    fun showSuccessMessage(message: String) = _successMessages.tryEmit(message)
}
