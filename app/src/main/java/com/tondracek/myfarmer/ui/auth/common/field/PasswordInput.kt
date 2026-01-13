package com.tondracek.myfarmer.ui.auth.common.field

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.core.domain.domainerror.ValidationError
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
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
        ),
    )
}