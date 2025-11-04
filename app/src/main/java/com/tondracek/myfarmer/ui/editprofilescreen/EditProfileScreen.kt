package com.tondracek.myfarmer.ui.editprofilescreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.common.model.ImageResource
import com.tondracek.myfarmer.contactinfo.domain.model.ContactInfo
import com.tondracek.myfarmer.contactinfo.domain.model.MediaLink
import com.tondracek.myfarmer.ui.common.image.ImageView
import com.tondracek.myfarmer.ui.common.layout.LoadingLayout
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview

@Composable
fun EditProfileScreen(
    state: EditProfileScreenState,
    onNameChange: (String) -> Unit = {},
    onProfilePictureChange: (ImageResource) -> Unit,
    onContactInfoChange: (ContactInfo) -> Unit,
    onSaveClick: () -> Unit,
) {
    when (state) {
        is EditProfileScreenState.Success -> SuccessScreen(
            state = state,
            onNameChange = onNameChange,
            onProfilePictureChange = onProfilePictureChange,
            onContactInfoChange = onContactInfoChange,
            onSaveClick = onSaveClick,
        )

        EditProfileScreenState.Loading -> LoadingLayout()
        is EditProfileScreenState.Error -> Text(text = state.result.userError)
    }
}

@Composable
private fun SuccessScreen(
    state: EditProfileScreenState.Success,
    onNameChange: (String) -> Unit,
    onProfilePictureChange: (ImageResource) -> Unit,
    onContactInfoChange: (ContactInfo) -> Unit,
    onSaveClick: () -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        ImageView(
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(CircleShape),
            imageResource = state.profilePicture
        )
        TextField(
            value = state.profilePicture.uri ?: "",
            onValueChange = { onProfilePictureChange(ImageResource(uri = it)) },
        )

        NameField(
            modifier = Modifier.fillMaxWidth(),
            name = state.name,
            onNameChange = onNameChange,
        )

        Button(onClick = onSaveClick) { Text(text = "Save") }
    }
}

@Composable
private fun NameField(
    modifier: Modifier = Modifier,
    name: String,
    onNameChange: (String) -> Unit,
) {
    TextField(
        modifier = modifier,
        value = name,
        onValueChange = { newValue -> onNameChange(newValue) },
    )
}

@Preview
@Composable
private fun EditProfileScreenPreview() {
    MyFarmerPreview {
        EditProfileScreen(
            state = EditProfileScreenState.Success(
                name = "JohnDoe",
                profilePicture = ImageResource(null),
                contactInfo = ContactInfo(
                    email = "john@doe.com",
                    phoneNumber = "+1234567890",
                    website = MediaLink("website", "www.johndoe.com"),
                    facebook = MediaLink("facebook", "fb.com/johndoe"),
                    instagram = MediaLink("instagram", "instagram.com/johndoe"),
                )
            ),
            onNameChange = {},
            onProfilePictureChange = {},
            onContactInfoChange = {},
            onSaveClick = {},
        )
    }
}