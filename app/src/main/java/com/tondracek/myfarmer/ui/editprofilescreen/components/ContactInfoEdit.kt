package com.tondracek.myfarmer.ui.editprofilescreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.contactinfo.domain.model.ContactInfo
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun ContactInfoEdit(
    contactInfo: ContactInfo,
    onContactInfoChange: (ContactInfo) -> Unit
) {
    Column(
        modifier = Modifier.padding(MyFarmerTheme.paddings.medium),
        verticalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.small)
    ) {
        Text(
            text = stringResource(R.string.edit_your_contact_information),
            style = MyFarmerTheme.typography.textMedium,
        )
        LinkPhoneButton(
            phone = contactInfo.phoneNumber,
            onPhoneChange = { onContactInfoChange(contactInfo.copy(phoneNumber = it)) }
        )

        LinkEmailButton(
            email = contactInfo.email,
            onEmailChange = { onContactInfoChange(contactInfo.copy(email = it)) }
        )

        LinkInstagramButton(
            link = contactInfo.instagramLink,
            onLinkClick = { onContactInfoChange(contactInfo.copy(instagramLink = it)) }
        )

        LinkWebsiteButton(
            website = contactInfo.website,
            onWebsiteChange = { onContactInfoChange(contactInfo.copy(website = it)) }
        )

        LinkFacebookButton(
            facebookLink = contactInfo.facebookLink,
            onFacebookChange = { onContactInfoChange(contactInfo.copy(facebookLink = it)) }
        )
    }
}

@Preview
@Composable
private fun ContactInfoEditPreview() {
    MyFarmerPreview {
        ContactInfoEdit(
            contactInfo = ContactInfo.EMPTY
        ) {}
    }
}