package com.tondracek.myfarmer.ui.shopdetailscreen.components.sections

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.contactinfo.domain.model.ContactInfo
import com.tondracek.myfarmer.systemuser.data.user0
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun ContactInfoSection(
    modifier: Modifier = Modifier,
    contactInfo: ContactInfo,
) {
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

        contactInfo.phoneNumber?.let { phoneNumber ->
            OpenLinkItem(
                title = contactInfo.phoneNumber,
                url = "tel:$phoneNumber",
            )
        }
        contactInfo.email?.let { email ->
            OpenLinkItem(
                title = contactInfo.email,
                url = "mailto:$email",
            )
        }
        contactInfo.website?.let { website ->
            OpenLinkItem(
                title = "Website",
                url = website,
            )
        }
        contactInfo.facebookLink?.let { facebook ->
            OpenLinkItem(
                title = "Facebook",
                url = facebook,
            )
        }
        contactInfo.instagramLink?.let { instagram ->
            OpenLinkItem(
                title = "Instagram",
                url = instagram,
            )
        }
    }
}

@Composable
private fun OpenLinkItem(
    modifier: Modifier = Modifier,
    title: String,
    url: String,
    hint: String = stringResource(R.string.click_here),
) {
    val uriHandler = LocalUriHandler.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = MyFarmerTheme.paddings.small)
            .clickable { uriHandler.openUri(url) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                imageVector = Icons.Default.TouchApp,
                contentDescription = "Open link icon",
                tint = MyFarmerTheme.colors.primary,
            )
            Text(
                text = hint,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Companion.Bold,
            )
        }
    }
}

@Preview
@Composable
private fun ContactInfoPreview() {
    MyFarmerPreview {
        ContactInfoSection(contactInfo = user0.contactInfo)
    }
}
