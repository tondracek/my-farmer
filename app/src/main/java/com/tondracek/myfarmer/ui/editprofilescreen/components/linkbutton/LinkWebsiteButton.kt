package com.tondracek.myfarmer.ui.editprofilescreen.components.linkbutton

import android.content.Context
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Web
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview

@Composable
fun LinkWebsiteButton(
    website: String?,
    onSaveLink: (String?) -> Unit
) {
    val context = LocalContext.current
    LinkEditButton(
        input = website,
        onSaveInput = { onSaveLink(it?.normalizeWebsite()) },
        validator = { isValidWebsite(it.normalizeWebsite()) },
        keyboardType = KeyboardType.Unspecified,
        color = Color(0xFFFF9E0F),
        enterValidInputSupportingText = stringResource(R.string.invalid_website_url),
        label = stringResource(R.string.website),
        iconVector = Icons.Default.Web,
        setLinkText = stringResource(R.string.add_website),
        openPreview = { openWebsite(context, it.normalizeWebsite()) },
        prettifyInput = ::prettifyWebsite,
    )
}

private fun String.normalizeWebsite(): String = when {
    isBlank() -> ""
    this.startsWith("http://") || this.startsWith("https://") -> this
    else -> "https://${this}"
}

private fun isValidWebsite(url: String): Boolean =
    android.util.Patterns.WEB_URL.matcher(url).matches()

private fun prettifyWebsite(website: String): String =
    website
        .removePrefix("http://")
        .removePrefix("https://")
        .removePrefix("www.")
        .substringBefore("/")
        .let { "$it/..." }

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
            onSaveLink = {}
        )
    }
}

@Preview
@Composable
private fun LinkedWebsiteButtonPreview() {
    MyFarmerPreview {
        LinkWebsiteButton(
            website = "https://myfarm.cz",
            onSaveLink = {}
        )
    }
}
