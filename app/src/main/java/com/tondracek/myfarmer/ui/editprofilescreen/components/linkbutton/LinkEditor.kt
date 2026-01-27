package com.tondracek.myfarmer.ui.editprofilescreen.components.linkbutton

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.toIconButtonColors

@Composable
fun LinkEditor(
    initialInput: String,
    onClose: () -> Unit,
    onSave: (String?) -> Unit,
    validator: (input: String) -> Boolean,
    label: String,
    enterValidInputSupportingText: String,
    color: Color,
    keyboardType: KeyboardType,
    iconVector: ImageVector,
    openPreview: ((input: String) -> Unit)?,
) {
    var input by remember { mutableStateOf(initialInput) }
    var touched by remember { mutableStateOf(false) }

    val isEmpty = remember(input) { input.isBlank() }
    val isValid = remember(input) { validator(input) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.small)
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = {
                    touched = true
                    input = it
                },
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                modifier = Modifier.weight(1f),
                singleLine = true,
                label = { Text(label) },
                isError = touched && !isValid && !isEmpty,
                supportingText = {
                    if (touched && !isValid && !isEmpty)
                        Text(
                            text = enterValidInputSupportingText,
                            color = MyFarmerTheme.colors.error
                        )
                }
            )

            if (openPreview != null) {
                AnimatedVisibility(isValid) {
                    IconButton(
                        onClick = { openPreview(input) },
                        colors = color.toIconButtonColors()
                    ) {
                        Icon(
                            imageVector = iconVector,
                            contentDescription = null
                        )
                    }
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.small)
        ) {
            Button(
                colors = MyFarmerTheme.buttonColors.secondary,
                onClick = onClose,
            ) {
                Text(stringResource(R.string.cancel))
            }

            Button(
                enabled = isValid || isEmpty,
                colors = MyFarmerTheme.buttonColors.custom(color),
                onClick = {
                    onSave(input.takeIf { it.isNotBlank() })
                    onClose()
                }
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }
}
