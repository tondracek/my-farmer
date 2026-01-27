package com.tondracek.myfarmer.ui.editprofilescreen.components

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Facebook
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.toIconButtonColors

private enum class FacebookUiState {
    NOT_SET,
    SET,
    EDITING
}

@Composable
fun LinkFacebookButton(
    facebookLink: String?,
    onFacebookChange: (String?) -> Unit
) {
    val initialLink = remember(facebookLink) { facebookLink.orEmpty() }

    var state by remember(initialLink) {
        mutableStateOf(
            when {
                initialLink.isBlank() -> FacebookUiState.NOT_SET
                else -> FacebookUiState.SET
            }
        )
    }

    AnimatedContent(
        targetState = state,
        label = "FacebookLinkAnimation",
    ) { s ->
        when (s) {
            FacebookUiState.NOT_SET,
            FacebookUiState.SET -> FacebookActionButton(
                link = initialLink,
                isSet = s == FacebookUiState.SET,
                onEdit = { state = FacebookUiState.EDITING }
            )

            FacebookUiState.EDITING -> FacebookEditor(
                initialLink = initialLink,
                onClose = {
                    state = when {
                        initialLink.isBlank() -> FacebookUiState.NOT_SET
                        else -> FacebookUiState.SET
                    }
                },
                onSave = { onFacebookChange(it) }
            )
        }
    }
}

@Composable
private fun FacebookActionButton(
    link: String,
    isSet: Boolean,
    onEdit: () -> Unit,
) {
    Button(
        colors = MyFarmerTheme.buttonColors.custom(Color(0xFF4267B2)),
        onClick = onEdit,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.extraSmall),
        ) {
            Icon(
                imageVector = Icons.Default.Facebook,
                contentDescription = null,
            )

            Text(
                text = when {
                    isSet -> shortenFacebookLink(link)
                    else -> stringResource(R.string.add_facebook)
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
private fun FacebookEditor(
    initialLink: String,
    onClose: () -> Unit,
    onSave: (String?) -> Unit,
) {
    val context = LocalContext.current

    var input by remember { mutableStateOf(initialLink) }
    var touched by remember { mutableStateOf(false) }

    val normalizedUrl = remember(input) { normalizeFacebookUrl(input) }
    val isValid = remember(normalizedUrl) {
        normalizedUrl.isEmpty() || isFacebookUrl(normalizedUrl)
    }

    Column(horizontalAlignment = Alignment.End) {
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
                label = { Text(stringResource(R.string.facebook_profile_link)) },
                isError = touched && !isValid,
                supportingText = {
                    if (touched && !isValid)
                        Text(
                            text = stringResource(R.string.enter_a_valid_facebook_profile_link),
                            color = MyFarmerTheme.colors.error
                        )
                }
            )

            AnimatedVisibility(isValid) {
                IconButton(
                    onClick = { openFacebook(context, normalizedUrl) },
                    colors = Color(0xFF4267B2).toIconButtonColors()
                ) {
                    Icon(
                        imageVector = Icons.Default.Facebook,
                        contentDescription = null
                    )
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
                enabled = isValid,
                colors = MyFarmerTheme.buttonColors.custom(Color(0xFF4267B2)),
                onClick = {
                    onSave(normalizedUrl.takeIf { it.isNotBlank() })
                    onClose()
                }
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }
}

/* -------------------- helpers -------------------- */

private fun normalizeFacebookUrl(input: String): String =
    when {
        input.isBlank() -> ""
        input.startsWith("http://") || input.startsWith("https://") -> input
        else -> "https://$input"
    }

private fun isFacebookUrl(url: String): Boolean {
    val uri = runCatching { url.toUri() }.getOrNull() ?: return false
    val host = uri.host ?: return false

    val isFacebookDomain =
        host == "fb.com" ||
                host.endsWith(".facebook.com") ||
                host == "facebook.com"

    return isFacebookDomain && !uri.path.isNullOrBlank()
}

private fun shortenFacebookLink(link: String): String =
    link
        .removePrefix("http://")
        .removePrefix("https://")
        .removePrefix("www.")
        .substringBefore("/")
        .let { "$it/..." }

private fun openFacebook(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    context.startActivity(intent)
}

/* -------------------- previews -------------------- */

@Preview
@Composable
private fun LinkFacebookButtonPreview() {
    MyFarmerPreview {
        LinkFacebookButton(
            facebookLink = null,
            onFacebookChange = {}
        )
    }
}

@Preview
@Composable
private fun LinkedFacebookButtonPreview() {
    MyFarmerPreview {
        LinkFacebookButton(
            facebookLink = "https://www.facebook.com/john.doe",
            onFacebookChange = {}
        )
    }
}
