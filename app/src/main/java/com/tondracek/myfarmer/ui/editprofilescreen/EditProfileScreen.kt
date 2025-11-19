package com.tondracek.myfarmer.ui.editprofilescreen

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.contactinfo.domain.model.ContactInfo
import com.tondracek.myfarmer.systemuser.data.user0
import com.tondracek.myfarmer.ui.common.image.ImageView
import com.tondracek.myfarmer.ui.common.layout.ErrorLayout
import com.tondracek.myfarmer.ui.common.layout.LoadingLayout
import com.tondracek.myfarmer.ui.core.appstate.LocalAppUiController
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun EditProfileScreen(
    state: EditProfileScreenState,
    onNameChange: (String) -> Unit = {},
    onProfilePictureChange: (ImageResource) -> Unit,
    onContactInfoChange: (ContactInfo) -> Unit,
    onLogout: () -> Unit,
    onSaveClick: () -> Unit,
) {
    when (state) {
        is EditProfileScreenState.Success -> SuccessScreen(
            state = state,
            onNameChange = onNameChange,
            onProfilePictureChange = onProfilePictureChange,
            onContactInfoChange = onContactInfoChange,
            onLogout = onLogout,
            onSaveClick = onSaveClick,
        )

        EditProfileScreenState.SavedSuccessfully -> SavedSuccessfullyLayout()
        EditProfileScreenState.Loading -> LoadingLayout()
        is EditProfileScreenState.Error -> ErrorLayout(error = state.result)
    }
}

@Composable
private fun SavedSuccessfullyLayout(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Card(
            modifier = modifier,
            colors = MyFarmerTheme.cardColors.secondary,
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Icon(
                    modifier = Modifier.size(48.dp),
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MyFarmerTheme.colors.success,
                )
                Text(
                    text = "Profile saved successfully!",
                    style = MyFarmerTheme.typography.textLarge,
                )
            }
        }
    }
}

@Composable
private fun SuccessScreen(
    state: EditProfileScreenState.Success,
    onNameChange: (String) -> Unit,
    onProfilePictureChange: (ImageResource) -> Unit,
    onContactInfoChange: (ContactInfo) -> Unit,
    onLogout: () -> Unit,
    onSaveClick: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .scrollable(state = scrollState, orientation = Orientation.Vertical),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "You are logged in as ${state.name}",
                style = MyFarmerTheme.typography.headerMedium
            )
            Button(onClick = onLogout) {
                Text(text = "Logout")
            }
        }
        ProfilePicture(
            state = state,
            onProfilePictureChange = onProfilePictureChange,
        )

        NameField(
            modifier = Modifier.fillMaxWidth(),
            name = state.name,
            onNameChange = onNameChange,
        )

        Button(onClick = onSaveClick) { Text(text = "Save") }
    }

    val appUiController = LocalAppUiController.current
    val title = stringResource(R.string.edit_profile)
    LaunchedEffect(Unit) {
        appUiController.updateTitle(title).updateTopBarPadding(true)
    }
}

@Composable
private fun ProfilePicture(
    state: EditProfileScreenState.Success,
    onProfilePictureChange: (ImageResource) -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        ImageView(
            modifier = Modifier
                .widthIn(max = 200.dp)
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(CircleShape),
            imageResource = state.profilePicture,
            contentScale = ContentScale.Crop,
        )
        TextField(
            value = state.profilePicture.uri ?: "",
            onValueChange = { onProfilePictureChange(ImageResource(uri = it)) },
        )
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
            state = user0.toUiState(),
            onNameChange = {},
            onProfilePictureChange = {},
            onContactInfoChange = {},
            onLogout = {},
            onSaveClick = {},
        )
    }
}


@Preview
@Composable
private fun EditProfileSavedScreenPreview() {
    MyFarmerPreview {
        EditProfileScreen(
            state = EditProfileScreenState.SavedSuccessfully,
            onNameChange = {},
            onProfilePictureChange = {},
            onContactInfoChange = {},
            onLogout = {},
            onSaveClick = {},
        )
    }
}