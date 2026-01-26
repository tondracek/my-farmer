package com.tondracek.myfarmer.ui.core.confirmationdialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tondracek.myfarmer.R

@Composable
fun ConfirmationDialog(
    confirmationDialog: ConfirmationDialogRequest?,
    onDismissRequest: () -> Unit,
) = confirmationDialog?.let { dialog ->
    AlertDialog(
        onDismissRequest = onDismissRequest,
        text = { Text(dialog.message) },
        confirmButton = {
            Button(
                onClick = {
                    onDismissRequest()
                    dialog.onConfirm()
                }
            ) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}