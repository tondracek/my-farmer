package com.tondracek.myfarmer.ui.auth.common.field

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.core.domain.domainerror.ValidationError
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.components.textFieldColors
import com.tondracek.myfarmer.ui.core.util.toUserFriendlyMessage

@Composable
fun PasswordInput(
    input: String,
    error: ValidationError?,
    onInputChanged: (String) -> Unit,
    label: String = stringResource(R.string.password),
    contentType: ContentType = ContentType.Password,
) {
    val context = LocalContext.current
    var passwordVisible by remember { mutableStateOf(false) }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { this.contentType = contentType },
        value = input,
        onValueChange = onInputChanged,
        label = { Text(label) },
        isError = error != null,
        supportingText = {
            if (error != null) Text(text = error.toUserFriendlyMessage(context))
        },
        colors = MyFarmerTheme.textFieldColors.primary,
        shape = RoundedCornerShape(MyFarmerTheme.paddings.medium),
        visualTransformation = when (passwordVisible) {
            true -> VisualTransformation.None
            false -> PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
        ),
        trailingIcon = {
            VisibilityIcon(
                passwordVisible = passwordVisible,
                onIconClick = { passwordVisible = !passwordVisible }
            )
        },
        singleLine = true,
    )
}

@Composable
private fun VisibilityIcon(

    passwordVisible: Boolean,
    onIconClick: () -> Unit,
) {
    IconButton(
        colors = MyFarmerTheme.iconButtonColors.primary,
        onClick = onIconClick
    ) {
        Icon(
            imageVector = when (passwordVisible) {
                true -> Icons.Default.VisibilityOff
                false -> Icons.Default.Visibility
            },
            contentDescription = if (passwordVisible) "Hide password" else "Show password"
        )
    }
}

@Preview
@Composable
private fun PasswordInputPreview() {
    MyFarmerPreview {
        PasswordInput(
            input = "MySecretPassword",
            error = null,
            onInputChanged = {},
        )
    }
}