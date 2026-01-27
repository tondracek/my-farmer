package com.tondracek.myfarmer.ui.editprofilescreen.components

import android.util.Patterns
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Phone
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

private enum class PhoneUiState {
    NOT_SET,
    SET,
    EDITING
}

@Composable
fun LinkPhoneButton(
    phone: String?,
    onPhoneChange: (String?) -> Unit
) {
    val initialPhone = remember(phone) { phone.orEmpty() }

    var state by remember(initialPhone) {
        mutableStateOf(
            when {
                initialPhone.isBlank() -> PhoneUiState.NOT_SET
                else -> PhoneUiState.SET
            }
        )
    }

    AnimatedContent(
        targetState = state,
        label = "PhoneLinkAnimation",
    ) { s ->
        when (s) {
            PhoneUiState.NOT_SET,
            PhoneUiState.SET -> PhoneActionButton(
                phone = initialPhone,
                isSet = s == PhoneUiState.SET,
                onEdit = { state = PhoneUiState.EDITING }
            )

            PhoneUiState.EDITING -> PhoneEditor(
                initialPhone = initialPhone,
                onSave = {
                    onPhoneChange(it)
                    state = when {
                        it.isNullOrBlank() -> PhoneUiState.NOT_SET
                        else -> PhoneUiState.SET
                    }
                }
            )
        }
    }
}

@Composable
private fun PhoneActionButton(
    phone: String,
    isSet: Boolean,
    onEdit: () -> Unit,
) {
    Button(
        colors = MyFarmerTheme.buttonColors.custom(Color(0xFF25D366)),
        onClick = onEdit,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.extraSmall),
        ) {
            Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = null,
            )

            Text(
                text = when (isSet) {
                    true -> phone
                    false -> stringResource(R.string.add_phone_number)
                }
            )

            if (isSet)
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                )
        }
    }
}

@Composable
private fun PhoneEditor(
    initialPhone: String,
    onSave: (String?) -> Unit,
) {
    var input by remember { mutableStateOf(initialPhone) }
    var touched by remember { mutableStateOf(false) }

    val isValid = remember(input) {
        input.isEmpty() || Patterns.PHONE.matcher(input).matches()
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
            label = { Text("Phone number") },
            isError = touched && !isValid,
            supportingText = {
                if (touched && !isValid) {
                    Text(
                        text = stringResource(R.string.invalid_phone_number),
                        color = MyFarmerTheme.colors.error
                    )
                }
            }
        )

        AnimatedVisibility(isValid) {
            Button(onClick = { onSave(input.takeIf { it.isNotBlank() }) }) {
                Text(stringResource(R.string.save))
            }
        }
    }
}

@Preview
@Composable
private fun LinkPhoneButtonPreview() {
    MyFarmerPreview {
        LinkPhoneButton(
            phone = null,
            onPhoneChange = {}
        )
    }
}

@Preview
@Composable
private fun LinkedPhoneButtonPreview() {
    MyFarmerPreview {
        LinkPhoneButton(
            phone = "+420 777 123 456",
            onPhoneChange = {}
        )
    }
}
