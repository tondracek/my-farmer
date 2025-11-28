package com.tondracek.myfarmer.ui.editprofilescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.contactinfo.domain.model.ContactInfo
import com.tondracek.myfarmer.systemuser.data.user0
import com.tondracek.myfarmer.ui.common.layout.ErrorLayout
import com.tondracek.myfarmer.ui.common.layout.LoadingLayout
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.editprofilescreen.components.ContactInfoEdit
import com.tondracek.myfarmer.ui.editprofilescreen.components.ProfilePictureEdit

@Composable
fun EditProfileScreen(
    state: EditProfileScreenState,
    onNameChange: (String) -> Unit = {},
    onProfilePictureChange: (ImageResource) -> Unit,
    onContactInfoChange: (ContactInfo) -> Unit,
    onLogout: () -> Unit,
    onSaveClick: () -> Unit,
    onNavigateBack: () -> Unit,
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

        EditProfileScreenState.UpdatingProfile -> UpdatingProfileLayout()
        EditProfileScreenState.SavedSuccessfully -> SavedSuccessfullyLayout()
        EditProfileScreenState.Loading -> LoadingLayout()
        is EditProfileScreenState.Error -> ErrorLayout(
            failure = state.result,
            onNavigateBack = onNavigateBack,
        )
    }
}

@Composable
private fun UpdatingProfileLayout(modifier: Modifier = Modifier) {
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
                CircularProgressIndicator(modifier = Modifier.size(48.dp))
                Text(
                    text = "Updating profile, please wait!",
                    style = MyFarmerTheme.typography.textLarge,
                )
            }
        }
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

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .verticalScroll(state = scrollState),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "You are logged in as ${state.name}",
                    style = MyFarmerTheme.typography.titleMedium
                )
                Button(onClick = onLogout) {
                    Text(text = "Logout")
                }
            }
            ProfilePictureEdit(
                state = state,
                onProfilePictureChange = onProfilePictureChange,
            )

            NameField(
                modifier = Modifier.fillMaxWidth(),
                name = state.name,
                onNameChange = onNameChange,
            )

            HorizontalDivider(Modifier.padding(horizontal = MyFarmerTheme.paddings.large))

            ContactInfoEdit(
                contactInfo = state.contactInfo,
                onContactInfoChange = onContactInfoChange,
            )

            Spacer(Modifier.height(64.dp))
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(MyFarmerTheme.paddings.medium),
            onClick = onSaveClick
        ) { Text(text = "Save") }
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
            text = "Edit your name:",
            style = MyFarmerTheme.typography.textMedium,
        )
        TextField(
            modifier = modifier,
            value = name,
            onValueChange = { newValue -> onNameChange(newValue) },
        )
    }
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
            onNavigateBack = {},
        )
    }
}

@Preview
@Composable
private fun EditProfileUpdatingScreenPreview() {
    MyFarmerPreview {
        EditProfileScreen(
            state = EditProfileScreenState.UpdatingProfile,
            onNameChange = {},
            onProfilePictureChange = {},
            onContactInfoChange = {},
            onLogout = {},
            onSaveClick = {},
            onNavigateBack = {},
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
            onNavigateBack = {},
        )
    }
}