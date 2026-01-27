package com.tondracek.myfarmer.ui.editprofilescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.contactinfo.domain.model.ContactInfo
import com.tondracek.myfarmer.ui.common.button.ButtonRow
import com.tondracek.myfarmer.ui.common.layout.CardMessageLayout
import com.tondracek.myfarmer.ui.common.layout.LoadingLayout
import com.tondracek.myfarmer.ui.common.scaffold.ScreenScaffold
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.editprofilescreen.components.LinkEmailButton
import com.tondracek.myfarmer.ui.editprofilescreen.components.LinkFacebookButton
import com.tondracek.myfarmer.ui.editprofilescreen.components.LinkInstagramButton
import com.tondracek.myfarmer.ui.editprofilescreen.components.LinkPhoneButton
import com.tondracek.myfarmer.ui.editprofilescreen.components.LinkWebsiteButton
import com.tondracek.myfarmer.ui.editprofilescreen.components.ProfilePictureEdit

@Composable
fun EditProfileScreen(
    state: EditProfileScreenState,
    onFormEvent: (EditProfileFormEvent) -> Unit,
    onScreenEvent: (EditProfileScreenEvent) -> Unit,
) {
    when (state) {
        is EditProfileScreenState.Success -> SuccessScreen(
            input = state.userInput,
            onNameChange = { onFormEvent(EditProfileFormEvent.OnNameChange(it)) },
            onProfilePictureChange = { onFormEvent(EditProfileFormEvent.OnProfilePictureChange(it)) },
            onContactInfoChange = { onFormEvent(EditProfileFormEvent.OnContactInfoChange(it)) },
            onCancelClicked = { onScreenEvent(EditProfileScreenEvent.OnCancelClicked) },
            onSaveClicked = { onScreenEvent(EditProfileScreenEvent.OnSaveClicked) },
        )

        EditProfileScreenState.UpdatingProfile -> UpdatingProfileLayout()
        EditProfileScreenState.Loading -> LoadingLayout()
    }
}

@Composable
private fun UpdatingProfileLayout() {
    ScreenScaffold(title = stringResource(R.string.edit_profile)) {
        CardMessageLayout {
            CircularProgressIndicator(modifier = Modifier.size(48.dp))
            Text(
                text = stringResource(R.string.updating_profile_please_wait),
                style = MyFarmerTheme.typography.textLarge,
            )
        }
    }
}

@Composable
private fun SuccessScreen(
    input: EditUserUiState,
    onNameChange: (String) -> Unit,
    onProfilePictureChange: (ImageResource) -> Unit,
    onContactInfoChange: (ContactInfo) -> Unit,
    onCancelClicked: () -> Unit,
    onSaveClicked: () -> Unit,
) {
    val scrollState = rememberScrollState()

    ScreenScaffold(
        title = stringResource(R.string.edit_profile),
        applyContentPaddingInternally = false,
        bottomBar = {
            ButtonRow(
                modifier = Modifier.fillMaxWidth(),
                buttonColors1 = MyFarmerTheme.buttonColors.error,
                text1 = stringResource(R.string.cancel),
                onClick1 = onCancelClicked,
                buttonColors2 = MyFarmerTheme.buttonColors.primary,
                text2 = stringResource(R.string.save),
                onClick2 = onSaveClicked,
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(state = scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(contentPadding.calculateTopPadding()))

            ProfilePictureEdit(
                profilePicture = input.profilePicture,
                onProfilePictureChange = onProfilePictureChange,
            )

            NameField(
                modifier = Modifier.fillMaxWidth(),
                name = input.name,
                onNameChange = onNameChange,
            )

            HorizontalDivider(Modifier.padding(horizontal = MyFarmerTheme.paddings.large))

            ContactInfoEdit(
                contactInfo = input.contactInfo,
                onContactInfoChange = onContactInfoChange,
            )

            Spacer(Modifier.height(contentPadding.calculateBottomPadding()))
        }
    }
}

@Composable
private fun NameField(
    modifier: Modifier = Modifier,
    name: String,
    onNameChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier.padding(MyFarmerTheme.paddings.medium),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(R.string.edit_your_name),
            style = MyFarmerTheme.typography.textMedium,
        )
        TextField(
            modifier = modifier,
            value = name,
            onValueChange = { newValue -> onNameChange(newValue) },
        )
    }
}

@Composable
private fun ContactInfoEdit(
    contactInfo: ContactInfo,
    onContactInfoChange: (ContactInfo) -> Unit
) {
    Column(
        modifier = Modifier.padding(MyFarmerTheme.paddings.medium),
        verticalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.small),
        horizontalAlignment = Alignment.CenterHorizontally,
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
private fun EditProfileScreenPreview() {
    MyFarmerPreview {
        EditProfileScreen(
            state = EditProfileScreenState.Success(
                userInput = EditUserUiState(
                    name = "John Doe",
                    profilePicture = ImageResource.EMPTY,
                    contactInfo = ContactInfo.EMPTY
                ),
                wasChanged = false
            ),
            onFormEvent = {},
            onScreenEvent = {},
        )
    }
}

@Preview
@Composable
private fun EditProfileUpdatingScreenPreview() {
    MyFarmerPreview {
        EditProfileScreen(
            state = EditProfileScreenState.UpdatingProfile,
            onFormEvent = {},
            onScreenEvent = {},
        )
    }
}
