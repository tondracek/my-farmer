package com.tondracek.myfarmer.ui.core.confirmationdialog

data class ConfirmationDialogRequest(
    val message: String,
    val onConfirm: () -> Unit,
)
