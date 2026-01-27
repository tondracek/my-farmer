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
import androidx.compose.material.icons.filled.Web
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

private enum class WebsiteUiState {
    NOT_SET,
    SET,
    EDITING
}

@Composable
fun LinkWebsiteButton(
    website: String?,
    onWebsiteChange: (String?) -> Unit
) {
    val initialWebsite = remember(website) { website.orEmpty().stripWebsite() }
    var state by remember(initialWebsite) {
        mutableStateOf(
            when {
                initialWebsite.isBlank() -> WebsiteUiState.NOT_SET
                else -> WebsiteUiState.SET
            }
        )
    }

    AnimatedContent(
        targetState = state,
        label = "WebsiteLinkAnimation",
    ) { s ->
        when (s) {
            WebsiteUiState.NOT_SET,
            WebsiteUiState.SET -> WebsiteActionButton(
                website = initialWebsite,
                isSet = s == WebsiteUiState.SET,
                onEdit = { state = WebsiteUiState.EDITING }
            )

            WebsiteUiState.EDITING -> WebsiteEditor(
                initialWebsite = initialWebsite,
                onClose = {
                    state = when {
                        initialWebsite.isBlank() -> WebsiteUiState.NOT_SET
                        else -> WebsiteUiState.SET
                    }
                },
                onSave = { onWebsiteChange(it) }
            )
        }
    }
}

@Composable
private fun WebsiteActionButton(
    website: String,
    isSet: Boolean,
    onEdit: () -> Unit,
) {
    Button(
        colors = MyFarmerTheme.buttonColors.custom(Color(0xFFFF9E0F)),
        onClick = onEdit,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.extraSmall),
        ) {
            Icon(
                imageVector = Icons.Default.Web,
                contentDescription = null,
            )

            Text(
                text = when (isSet) {
                    true -> shortenWebsite(website)
                    false -> stringResource(R.string.add_website)
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
private fun WebsiteEditor(
    initialWebsite: String,
    onClose: () -> Unit,
    onSave: (String?) -> Unit,
) {
    val context = LocalContext.current

    var input by remember { mutableStateOf(initialWebsite) }
    var touched by remember { mutableStateOf(false) }

    val normalizedUrl = remember(input) { normalizeWebsite(input) }
    val isEmpty = remember(normalizedUrl) { normalizedUrl.isBlank() }
    val isValid = remember(normalizedUrl) { isValidWebsite(normalizedUrl) }

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
                label = { Text(stringResource(R.string.website)) },
                isError = touched && !isValid,
                supportingText = {
                    if (touched && !isValid)
                        Text(
                            text = stringResource(R.string.invalid_website_url),
                            color = MyFarmerTheme.colors.error
                        )
                }
            )

            AnimatedVisibility(isValid) {
                IconButton(
                    onClick = { openWebsite(context, normalizedUrl) },
                    colors = Color(0xFFFF9E0F).toIconButtonColors()
                ) {
                    Icon(
                        imageVector = Icons.Default.Web,
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
                enabled = isValid || isEmpty,
                colors = MyFarmerTheme.buttonColors.custom(Color(0xFFFF9E0F)),
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

private fun normalizeWebsite(input: String): String =
    when {
        input.isBlank() -> ""
        input.startsWith("http://") || input.startsWith("https://") -> input
        else -> "https://$input"
    }

private fun isValidWebsite(url: String): Boolean =
    runCatching { url.toUri() }.isSuccess

private fun shortenWebsite(website: String): String =
    website
        .stripWebsite()
        .let { "$it/..." }

private fun String.stripWebsite(): String =
    this
        .removePrefix("http://")
        .removePrefix("https://")
        .removePrefix("www.")
        .substringBefore("/")

private fun openWebsite(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    context.startActivity(intent)
}

@Preview
@Composable
private fun LinkWebsiteButtonPreview() {
    MyFarmerPreview {
        LinkWebsiteButton(
            website = null,
            onWebsiteChange = {}
        )
    }
}

@Preview
@Composable
private fun LinkedWebsiteButtonPreview() {
    MyFarmerPreview {
        LinkWebsiteButton(
            website = "https://myfarm.cz",
            onWebsiteChange = {}
        )
    }
}
