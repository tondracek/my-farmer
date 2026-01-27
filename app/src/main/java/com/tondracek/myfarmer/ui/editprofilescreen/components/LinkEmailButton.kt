package com.tondracek.myfarmer.ui.editprofilescreen.components

import android.util.Patterns
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

private enum class EmailUiState {
    NOT_SET,
    SET,
    EDITING
}

@Composable
fun LinkEmailButton(
    email: String?,
    onEmailChange: (String?) -> Unit
) {
    val initialEmail = remember(email) { email.orEmpty() }
    var state by remember(initialEmail) {
        mutableStateOf(
            when {
                initialEmail.isBlank() -> EmailUiState.NOT_SET
                else -> EmailUiState.SET
            }
        )
    }

    AnimatedContent(
        targetState = state,
        label = "EmailEditorAnimation",
    ) { s ->
        when (s) {
            EmailUiState.NOT_SET,
            EmailUiState.SET -> EmailActionButton(
                email = initialEmail,
                isSet = s == EmailUiState.SET,
                onEdit = { state = EmailUiState.EDITING }
            )

            EmailUiState.EDITING -> EmailEditorInput(
                initialEmail = initialEmail,
                onSave = {
                    onEmailChange(it)
                    state = when {
                        it.isNullOrBlank() -> EmailUiState.NOT_SET
                        else -> EmailUiState.SET
                    }
                }
            )
        }
    }
}

@Composable
private fun EmailActionButton(
    email: String,
    isSet: Boolean,
    onEdit: () -> Unit,
) {
    Button(
        colors = MyFarmerTheme.buttonColors.custom(Color(0xFFBB001B)),
        onClick = onEdit,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.extraSmall),
        ) {
            Icon(
                imageVector = Icons.Default.Mail,
                contentDescription = null,
            )

            Text(
                text = when (isSet) {
                    true -> email
                    false -> "Set email"
                }
            )

            if (isSet) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
private fun EmailEditorInput(
    initialEmail: String,
    onSave: (String?) -> Unit,
) {
    var input by remember { mutableStateOf(initialEmail) }
    var touched by remember { mutableStateOf(false) }

    val isValid = remember(input) {
        input.isEmpty() || isValidEmail(input)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.small)
    ) {
        OutlinedTextField(
            value = input,
            onValueChange = {
                touched = true
                input = it
            },
            modifier = Modifier.weight(1f),
            singleLine = true,
            label = { Text(stringResource(R.string.email)) },
            isError = touched && !isValid,
            supportingText = {
                if (touched && !isValid) {
                    Text(
                        stringResource(R.string.enter_a_valid_email),
                        color = MyFarmerTheme.colors.error
                    )
                }
            }
        )

        AnimatedVisibility(isValid) {
            Button(
                onClick = {
                    onSave(input.takeIf { it.isNotBlank() })
                }
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }
}

private fun isValidEmail(email: String): Boolean =
    Patterns.EMAIL_ADDRESS.matcher(email).matches()

@Preview
@Composable
private fun EmailEditorPreview() {
    MyFarmerPreview {
        LinkEmailButton(
            email = null,
            onEmailChange = {}
        )
    }
}

@Preview
@Composable
private fun EmailEditorSetPreview() {
    MyFarmerPreview {
        LinkEmailButton(
            email = "hello@example.com",
            onEmailChange = {}
        )
    }
}
