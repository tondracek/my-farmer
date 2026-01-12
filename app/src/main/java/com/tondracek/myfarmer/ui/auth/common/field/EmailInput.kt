package com.tondracek.myfarmer.ui.auth.common.field

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.components.textFieldColors

@Composable
fun EmailInput(
    input: String,
    error: String?,
    onInputChanged: (String) -> Unit,
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { contentType = ContentType.EmailAddress },
        value = input,
        onValueChange = onInputChanged,
        label = { Text(stringResource(R.string.email)) },
        isError = error != null,
        supportingText = {
            if (error != null) Text(text = error)
        },
        colors = MyFarmerTheme.textFieldColors.primary,
        shape = RoundedCornerShape(MyFarmerTheme.paddings.medium),

        )
}