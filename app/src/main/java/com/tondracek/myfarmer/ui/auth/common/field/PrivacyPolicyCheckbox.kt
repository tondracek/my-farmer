package com.tondracek.myfarmer.ui.auth.registrationscreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.core.domain.domainerror.ValidationError
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.core.util.toUserFriendlyMessage

private const val PRIVACY_URL =
    "https://developerthomas.cz/privacy_policy/my_farmer.html"

@Composable
fun PrivacyPolicyCheckbox(
    checked: Boolean,
    error: ValidationError?,
    onCheckedChange: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange
            )

            Text(
                modifier = Modifier
                    .padding(start = MyFarmerTheme.paddings.small)
                    .clickable {
                        uriHandler.openUri(PRIVACY_URL)
                    },
                text = buildAnnotatedString {
                    append(stringResource(R.string.i_agree_with))
                    append(" ")

                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline,
                        )
                    ) {
                        append(stringResource(R.string.privacy_policy))
                    }
                }
            )
        }

        if (error != null) {
            Text(
                modifier = Modifier.padding(start = 48.dp),
                text = error.toUserFriendlyMessage(context),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}