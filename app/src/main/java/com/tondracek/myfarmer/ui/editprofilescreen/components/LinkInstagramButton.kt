package com.tondracek.myfarmer.ui.editprofilescreen.components

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.toIconButtonColors

private enum class InstagramUiState {
    NOT_LINKED,
    LINKED,
    EDITING
}

@Composable
fun LinkInstagramButton(
    link: String?,
    onLinkClick: (String?) -> Unit
) {
    val initialUsername = remember(link) { link?.toInstagramUsername().orEmpty() }
    var state by remember(initialUsername) {
        mutableStateOf(
            when {
                initialUsername.isBlank() -> InstagramUiState.NOT_LINKED
                else -> InstagramUiState.LINKED
            }
        )
    }

    AnimatedContent(
        targetState = state,
        label = "InstagramLinkAnimation",
    ) { s ->
        when (s) {
            InstagramUiState.NOT_LINKED,
            InstagramUiState.LINKED -> InstagramActionButton(
                username = initialUsername,
                isLinked = s == InstagramUiState.LINKED,
                onEdit = { state = InstagramUiState.EDITING }
            )

            InstagramUiState.EDITING -> InstagramEditor(
                initialUsername = initialUsername,
                onSave = {
                    onLinkClick(it)
                    state = when (it) {
                        null -> InstagramUiState.NOT_LINKED
                        else -> InstagramUiState.LINKED
                    }
                }
            )
        }
    }
}

@Composable
private fun InstagramActionButton(
    username: String,
    isLinked: Boolean,
    onEdit: () -> Unit,
) {
    Button(
        colors = MyFarmerTheme.buttonColors.custom(Color(0xFFE1306C)),
        onClick = onEdit,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.extraSmall),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.instagram),
                contentDescription = null,
            )

            Text(
                text = when (isLinked) {
                    true -> username
                    false -> stringResource(R.string.link_instagram)
                }
            )

            if (isLinked)
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                )
        }
    }
}

@Composable
private fun InstagramEditor(
    initialUsername: String,
    onSave: (String?) -> Unit,
) {
    val context = LocalContext.current

    var input by remember { mutableStateOf(initialUsername) }
    var touched by remember { mutableStateOf(false) }

    val username = remember(input) { input.toInstagramUsername() }
    val isValid = remember(username) {
        username.isEmpty() || isUserNameValid(username)
    }

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
            label = { Text(stringResource(R.string.instagram_username)) },
            leadingIcon = {
                Text("@", style = MyFarmerTheme.typography.textLarge)
            },
            isError = touched && !isValid,
            supportingText = {
                if (touched && !isValid) {
                    Text(
                        stringResource(R.string.enter_a_valid_instagram_username),
                        color = MyFarmerTheme.colors.error
                    )
                }
            }
        )

        AnimatedVisibility(isValid) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.small)
            ) {
                IconButton(
                    onClick = {
                        openInstagramProfile(
                            context,
                            username,
                            username.toInstagramUrl()
                        )
                    },
                    colors = Color(0xFFE1306C).toIconButtonColors()
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.instagram),
                        contentDescription = null
                    )
                }

                Button(
                    onClick = {
                        onSave(
                            username.takeIf { it.isNotBlank() }
                                ?.toInstagramUrl()
                        )
                    }
                ) {
                    Text(stringResource(R.string.save))
                }
            }
        }
    }
}

private fun String.toInstagramUsername() = trim()
    .removePrefix("@")
    .substringAfter("instagram.com/", this)
    .substringBefore("/")
    .lowercase()

private fun String.toInstagramUrl() =
    "https://www.instagram.com/$this"

private fun isUserNameValid(username: String) =
    username.length in 1..30 &&
            username.matches(Regex("^[a-z0-9._]+$")) &&
            !username.startsWith(".") &&
            !username.endsWith(".")

private fun openInstagramProfile(
    context: Context,
    username: String,
    fallbackUrl: String
) {
    val appIntent = Intent(
        Intent.ACTION_VIEW,
        "instagram://user?username=$username".toUri()
    )

    val browserIntent = Intent(
        Intent.ACTION_VIEW,
        fallbackUrl.toUri()
    )

    try {
        context.startActivity(appIntent)
    } catch (_: Exception) {
        context.startActivity(browserIntent)
    }
}

@Preview
@Composable
private fun LinkInstagramButtonPreview() {
    MyFarmerPreview {
        LinkInstagramButton(
            link = null,
            onLinkClick = {}
        )
    }
}

@Preview
@Composable
private fun LinkedInstagramButtonPreview() {
    MyFarmerPreview {
        LinkInstagramButton(
            link = "https://www.instagram.com/myusername",
            onLinkClick = {}
        )
    }
}
