package com.tondracek.myfarmer.ui.editprofilescreen.components.linkbutton

import android.content.Context
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Facebook
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
fun LinkFacebookButton(
    facebookLink: String?,
    onSaveLink: (String?) -> Unit
) {
    val context = LocalContext.current
    LinkEditButton(
        input = facebookLink,
        onSaveInput = { onSaveLink(it?.let { normalizeFacebookUrl(it) }) },
        validator = { isFacebookUrl(normalizeFacebookUrl(it)) },
        keyboardType = KeyboardType.Unspecified,
        color = Color(0xFF1877F2),
        enterValidInputSupportingText = stringResource(R.string.enter_a_valid_facebook_profile_link),
        label = stringResource(R.string.facebook),
        iconVector = Icons.Default.Facebook,
        setLinkText = stringResource(R.string.add_facebook),
        openPreview = { openFacebook(context, normalizeFacebookUrl(it)) },
        prettifyInput = ::prettifyFacebookLink,
    )
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

    val isFacebookDomain = host == "fb.com" ||
            host.endsWith(".facebook.com") ||
            host == "facebook.com"

    return isFacebookDomain && !uri.path.isNullOrBlank()
}

private fun prettifyFacebookLink(link: String): String =
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
            onSaveLink = {}
        )
    }
}

@Preview
@Composable
private fun LinkedFacebookButtonPreview() {
    MyFarmerPreview {
        LinkFacebookButton(
            facebookLink = "https://www.facebook.com/john.doe",
            onSaveLink = {}
        )
    }
}
