package com.tondracek.myfarmer.ui.editprofilescreen.components.linkbutton

import android.util.Patterns
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview

@Composable
fun LinkEmailButton(
    email: String?,
    onSaveEmail: (String?) -> Unit
) {
    LinkEditButton(
        input = email,
        onSaveInput = onSaveEmail,
        validator = { Patterns.EMAIL_ADDRESS.matcher(it).matches() },
        keyboardType = KeyboardType.Email,
        color = Color(0xFFBB001B),
        enterValidInputSupportingText = stringResource(R.string.enter_a_valid_email),
        label = stringResource(R.string.email),
        iconVector = Icons.Default.Mail,
        setLinkText = stringResource(R.string.set_email),
        openPreview = null,
        prettifyInput = null,
    )
}

@Preview
@Composable
private fun EmailEditorPreview() {
    MyFarmerPreview {
        LinkEmailButton(
            email = null,
            onSaveEmail = {}
        )
    }
}

@Preview
@Composable
private fun EmailEditorSetPreview() {
    MyFarmerPreview {
        LinkEmailButton(
            email = "hello@example.com",
            onSaveEmail = {}
        )
    }
}
