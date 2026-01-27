package com.tondracek.myfarmer.ui.editprofilescreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Edit your contact information:",
            style = MyFarmerTheme.typography.textMedium,
        )
        OutlinedTextField(
            value = contactInfo.phoneNumber.orEmpty(),
            onValueChange = { onContactInfoChange(contactInfo.copy(phoneNumber = it)) },
            label = { Text("Phone number") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )

        EmailEditor(
            email = contactInfo.email,
            onEmailChange = { onContactInfoChange(contactInfo.copy(email = it)) }
        )

        LinkInstagramButton(
            link = contactInfo.instagramLink,
            onLinkClick = { onContactInfoChange(contactInfo.copy(instagramLink = it)) }
        )

        OutlinedTextField(
            value = contactInfo.website.orEmpty(),
            onValueChange = { onContactInfoChange(contactInfo.copy(website = it)) },
            label = { Text("Website") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = contactInfo.facebookLink.orEmpty(),
            onValueChange = { onContactInfoChange(contactInfo.copy(facebookLink = it)) },
            label = { Text("Facebook") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
            modifier = Modifier.fillMaxWidth()
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