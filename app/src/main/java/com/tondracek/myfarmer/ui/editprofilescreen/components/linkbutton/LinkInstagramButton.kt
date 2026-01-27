package com.tondracek.myfarmer.ui.editprofilescreen.components.linkbutton

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview

@Composable
fun LinkInstagramButton(
    link: String?,
    onSaveLink: (String?) -> Unit
) {
    val context = LocalContext.current

    LinkEditButton(
        input = link?.toInstagramUsername(),
        onSaveInput = { onSaveLink(it?.toInstagramLink()) },
        validator = { isUserNameValid(it.parseUsername()) },
        keyboardType = KeyboardType.Unspecified,
        color = Color(0xFFC13584),
        enterValidInputSupportingText = stringResource(R.string.enter_a_valid_instagram_username),
        label = stringResource(R.string.instagram_username),
        iconVector = ImageVector.vectorResource(R.drawable.instagram),
        setLinkText = stringResource(R.string.link_instagram),
        openPreview = { openInstagramProfile(context, it.parseUsername()) },
        prettifyInput = { "@${it.parseUsername()}" },
    )
}

private fun String.toInstagramLink() = "https://www.instagram.com/${this.parseUsername()}"

private fun String.toInstagramUsername() = trim()
    .removePrefix("@")
    .substringAfter("instagram.com/", this)
    .substringBefore("/")
    .lowercase()

private fun String.parseUsername() = trim()
    .removePrefix("@")
    .substringBefore("/")

private fun isUserNameValid(username: String) =
    username.length in 1..30 &&
            username.matches(Regex("^[a-z0-9._]+$")) &&
            !username.startsWith(".") &&
            !username.endsWith(".")

private fun openInstagramProfile(
    context: Context,
    username: String,
) {
    val appIntent = Intent(
        Intent.ACTION_VIEW,
        "instagram://user?username=$username".toUri(),
    )

    val browserIntent = Intent(
        Intent.ACTION_VIEW,
        "https://www.instagram.com/$username".toUri(),
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
            onSaveLink = {},
        )
    }
}

@Preview
@Composable
private fun LinkedInstagramButtonPreview() {
    MyFarmerPreview {
        LinkInstagramButton(
            link = "https://www.instagram.com/myusername",
            onSaveLink = {},
        )
    }
}
