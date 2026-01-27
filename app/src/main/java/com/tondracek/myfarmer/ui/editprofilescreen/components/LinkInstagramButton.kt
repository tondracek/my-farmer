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

private sealed interface InstagramLinkState {
    data object NotLinked : InstagramLinkState
    data class Linked(val username: String) : InstagramLinkState
    data object Linking : InstagramLinkState
}

@Composable
fun LinkInstagramButton(
    link: String?,
    onLinkClick: (String?) -> Unit
) {
    var state by remember(link) {
        mutableStateOf(
            when {
                link.isNullOrBlank() -> InstagramLinkState.NotLinked
                else -> InstagramLinkState.Linked(link.toInstagramUsername())
            }
        )
    }

    AnimatedContent(
        targetState = state,
        label = "LinkInstagramButtonAnimation"
    ) { s ->
        when (s) {
            is InstagramLinkState.Linked -> InstagramButton(
                state = s,
                onStateChange = { state = it }
            )

            InstagramLinkState.Linking -> InstagramInput(
                link = link.orEmpty(),
                onSaveLink = {
                    onLinkClick(it)
                    state = when (it) {
                        null -> InstagramLinkState.NotLinked
                        else -> InstagramLinkState.Linked(it.toInstagramUsername())
                    }
                }
            )

            InstagramLinkState.NotLinked -> InstagramButton(
                state = s,
                onStateChange = { state = it }
            )
        }
    }
}

@Composable
private fun InstagramButton(
    state: InstagramLinkState,
    onStateChange: (InstagramLinkState) -> Unit
) {
    Button(
        colors = MyFarmerTheme.buttonColors.custom(Color(0xFFE1306C)),
        onClick = { onStateChange(InstagramLinkState.Linking) }
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
                text = when (state) {
                    is InstagramLinkState.Linked -> state.username
                    else -> stringResource(R.string.link_instagram)
                }
            )

            if (state is InstagramLinkState.Linked)
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                )
        }
    }
}

private fun String.toInstagramUsername() = this
    .trim()
    .removePrefix("@")
    .substringAfter("instagram.com/", this)
    .substringBefore("/")
    .lowercase()

private fun String.toInstagramUrl() = "https://www.instagram.com/$this"

private fun isUserNameValid(username: String): Boolean = username.isNotBlank() &&
        username.length in 1..30 &&
        username.matches(Regex("^[a-z0-9._]+$")) &&
        !username.startsWith(".") &&
        !username.endsWith(".")

@Composable
private fun InstagramInput(
    link: String,
    onSaveLink: (String?) -> Unit,
) {
    val context = LocalContext.current

    var input by remember { mutableStateOf(link.toInstagramUsername()) }
    var touched by remember { mutableStateOf(false) }

    val username = remember(input) {
        input.toInstagramUsername()
    }

    val isFormatValid = remember(username) {
        isUserNameValid(username) || username.isEmpty()
    }

    val profileUrl = "https://www.instagram.com/$username"

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
            singleLine = true,
            label = { Text("Instagram username") },
            leadingIcon = {
                Text(
                    text = "@",
                    style = MyFarmerTheme.typography.textLarge,
                )
            },
            isError = touched && !isFormatValid,
            supportingText = {
                if (touched && !isFormatValid) {
                    Text(
                        text = stringResource(R.string.enter_a_valid_instagram_username),
                        color = MyFarmerTheme.colors.error
                    )
                }
            },
            modifier = Modifier.weight(1f)
        )

        AnimatedVisibility(isFormatValid) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.small)
            ) {
                IconButton(
                    onClick = {
                        openInstagramProfile(
                            context = context,
                            username = username,
                            fallbackUrl = profileUrl
                        )
                    },
                    colors = Color(0xFFE1306C).toIconButtonColors()
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.instagram),
                        contentDescription = null
                    )
                }

                Button(onClick = {
                    val newLink: String? = username.takeIf { it.isNotBlank() }?.toInstagramUrl()
                    onSaveLink(newLink)
                }) {
                    Text(stringResource(R.string.save))
                }
            }
        }
    }
}

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
