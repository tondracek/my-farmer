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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.core.domain.domainerror.ValidationError
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.components.textFieldColors
import com.tondracek.myfarmer.ui.core.util.toUserFriendlyMessage

@Composable
fun EmailInput(
    input: String,
    error: ValidationError?,
    onInputChanged: (String) -> Unit,
) {
    val context = LocalContext.current

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { contentType = ContentType.EmailAddress },
        value = input,
        onValueChange = onInputChanged,
        label = { Text(stringResource(R.string.email)) },
        isError = error != null,
        supportingText = {
            if (error != null) Text(text = error.toUserFriendlyMessage(context))
        },
        colors = MyFarmerTheme.textFieldColors.primary,
        shape = RoundedCornerShape(MyFarmerTheme.paddings.medium),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done,
        ),
        singleLine = true,
    )
}