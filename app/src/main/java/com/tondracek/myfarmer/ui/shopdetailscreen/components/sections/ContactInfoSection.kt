package com.tondracek.myfarmer.ui.shopdetailscreen.components.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.contactinfo.domain.model.ContactInfo
import com.tondracek.myfarmer.ui.common.sample.user0
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun ContactInfoSection(
    modifier: Modifier = Modifier,
    contactInfo: ContactInfo,
    showErrorMessage: (String) -> Unit,
) {
    val uriHandler: UriHandler = LocalUriHandler.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = MyFarmerTheme.paddings.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.contact_info),
            style = MyFarmerTheme.typography.titleMedium,
        )


        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.extraSmall),
            verticalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.extraSmall),
        ) {

            contactInfo.phoneNumber.takeIf { !it.isNullOrBlank() }?.let { phoneNumber ->
                LinkButton(
                    uriHandler = uriHandler,
                    label = phoneNumber,
                    uri = "tel:$phoneNumber",
                    buttonColors = MyFarmerTheme.buttonColors.custom(Color(0xFF25D366)),
                    showErrorMessage = showErrorMessage,
                    icon = Icons.Default.Phone,
                )
            }

            contactInfo.email.takeIf { !it.isNullOrBlank() }?.let { email ->
                LinkButton(
                    uriHandler = uriHandler,
                    label = email,
                    uri = "mailto:$email",
                    buttonColors = MyFarmerTheme.buttonColors.custom(Color(0xFFBB001B)),
                    showErrorMessage = showErrorMessage,
                    icon = Icons.Default.Mail,
                )
            }

            contactInfo.facebookLink.takeIf { !it.isNullOrBlank() }?.let { facebook ->
                LinkButton(
                    uriHandler = uriHandler,
                    label = "Facebook",
                    uri = parseWebUri(facebook),
                    buttonColors = MyFarmerTheme.buttonColors.custom(Color(0xFF4267B2)),
                    showErrorMessage = showErrorMessage,
                    icon = Icons.Default.Facebook,
                )
            }

            contactInfo.instagramLink.takeIf { !it.isNullOrBlank() }?.let { instagram ->
                LinkButton(
                    uriHandler = uriHandler,
                    label = "Instagram",
                    uri = parseWebUri(instagram),
                    buttonColors = MyFarmerTheme.buttonColors.custom(Color(0xFFE1306C)),
                    showErrorMessage = showErrorMessage,
                    icon = ImageVector.vectorResource(R.drawable.instagram),
                )
            }

            contactInfo.website.takeIf { !it.isNullOrBlank() }?.let { website ->
                LinkButton(
                    uriHandler = uriHandler,
                    label = shortenWebLink(website),
                    uri = parseWebUri(website),
                    buttonColors = MyFarmerTheme.buttonColors.custom(Color(0xFFFF9E0F)),
                    showErrorMessage = showErrorMessage,
                    icon = Icons.Default.Web,
                )
            }
        }
    }
}

@Composable
private fun LinkButton(
    uriHandler: UriHandler,
    label: String,
    uri: String,
    buttonColors: ButtonColors,
    showErrorMessage: (String) -> Unit,
    icon: ImageVector,
) {
    val errorMessage = stringResource(R.string.could_not_open_the_link, uri)

    Button(
        colors = buttonColors,
        onClick = {
            uriHandler.safelyOpenUrl(uri) { showErrorMessage(errorMessage) }
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.extraSmall),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
            )
            Text(text = label)
        }
    }
}

private fun UriHandler.safelyOpenUrl(
    url: String,
    onError: () -> Unit
) = runCatching {
    this.openUri(url)
}.onFailure { onError() }

private fun parseWebUri(website: String): String {
    return when {
        website.startsWith("http://") || website.startsWith("https://") -> website
        else -> "https://$website"
    }
}

private fun shortenWebLink(link: String): String {
    return link
        .removePrefix("http://")
        .removePrefix("https://")
        .removePrefix("www.")
        .split("/")
        .firstOrNull()
        ?.let { "$it/..." }
        ?: link
}

@Preview
@Composable
private fun ContactInfoPreview() {
    MyFarmerPreview {
        ContactInfoSection(
            contactInfo = user0.contactInfo,
            showErrorMessage = {}
        )
    }
}
