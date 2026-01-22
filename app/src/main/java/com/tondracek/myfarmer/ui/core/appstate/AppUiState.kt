package com.tondracek.myfarmer.ui.core.appstate

import androidx.compose.runtime.compositionLocalOf
import com.tondracek.myfarmer.core.domain.domainerror.DomainError
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

val LocalAppUiController = compositionLocalOf<AppUiController> {
    error("AppUiController not provided")
}

class AppUiController {

    private val _errors = MutableSharedFlow<DomainError>(extraBufferCapacity = 1)
    val errors = _errors.asSharedFlow()

    fun showError(error: DomainError) = _errors.tryEmit(error)

    private val _errorMessages = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val errorMessages = _errorMessages.asSharedFlow()

    fun showErrorMessage(message: String) = _errorMessages.tryEmit(message)

    private val _successMessages = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val successMessages = _successMessages.asSharedFlow()

    fun showSuccessMessage(message: String) = _successMessages.tryEmit(message)

    private val _confirmationDialogs =
        MutableSharedFlow<ConfirmationDialogRequest>(extraBufferCapacity = 1)
    val confirmationDialogs = _confirmationDialogs.asSharedFlow()

    fun raiseConfirmationDialog(
        message: String,
        onConfirm: () -> Unit,
    ) = _confirmationDialogs.tryEmit(
        ConfirmationDialogRequest(
            message = message,
            onConfirm = onConfirm,
        )
    )
}

data class ConfirmationDialogRequest(
    val message: String,
    val onConfirm: () -> Unit,
)
