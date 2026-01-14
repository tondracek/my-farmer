package com.tondracek.myfarmer.ui.auth.loginscreen.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.auth.common.field.EmailInput

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordDialog(
    initialEmail: String = "",
    onDismissRequest: () -> Unit,
    onSendEmailClicked: (String) -> Unit,
) {
    var emailInput by remember { mutableStateOf(initialEmail) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        text = {
            EmailInput(
                input = emailInput,
                error = null,
                onInputChanged = { emailInput = it },
            )
        },
        confirmButton = {
            Button(
                onClick = { onSendEmailClicked(emailInput) },
            ) {
                Text(text = stringResource(R.string.send_password_reset_email))
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}