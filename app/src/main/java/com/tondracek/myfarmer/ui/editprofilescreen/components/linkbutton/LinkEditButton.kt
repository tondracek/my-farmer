package com.tondracek.myfarmer.ui.editprofilescreen.components.linkbutton

import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType

private enum class LinkButtonState {
    NOT_SET,
    SET,
    EDITING,
}


@Composable
fun LinkEditButton(
    input: String?,
    onSaveInput: (String?) -> Unit,
    validator: (String) -> Boolean,
    prettifyInput: ((String) -> String)?,
    keyboardType: KeyboardType,
    color: Color,
    enterValidInputSupportingText: String,
    label: String,
    iconVector: ImageVector,
    setLinkText: String,
    openPreview: ((String) -> Unit)?
) {
    val initialInput = remember(input) { input.orEmpty() }
    var state by remember(initialInput) {
        mutableStateOf(
            when {
                initialInput.isBlank() -> LinkButtonState.NOT_SET
                else -> LinkButtonState.SET
            }
        )
    }

    AnimatedContent(
        targetState = state,
        label = "LinkEditorAnimation",
    ) { s ->
        when (s) {
            LinkButtonState.NOT_SET,
            LinkButtonState.SET -> LinkActionButton(
                text = initialInput,
                isSet = s == LinkButtonState.SET,
                onEdit = { state = LinkButtonState.EDITING },
                setLinkText = setLinkText,
                iconVector = iconVector,
                color = color,
                prettifyInput = prettifyInput ?: { it },
            )

            LinkButtonState.EDITING -> LinkEditor(
                initialInput = initialInput,
                onClose = {
                    state = when {
                        initialInput.isBlank() -> LinkButtonState.NOT_SET
                        else -> LinkButtonState.SET
                    }
                },
                onSave = onSaveInput,
                validator = validator,
                label = label,
                enterValidInputSupportingText = enterValidInputSupportingText,
                color = color,
                keyboardType = keyboardType,
                iconVector = iconVector,
                openPreview = openPreview,
            )
        }
    }
}