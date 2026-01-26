package com.tondracek.myfarmer.ui.profilescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.ui.common.image.ImageView
import com.tondracek.myfarmer.ui.common.layout.LoadingLayout
import com.tondracek.myfarmer.ui.common.scaffold.ScreenScaffold
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.shopdetailscreen.components.sections.ContactInfoSection

@Composable
fun ProfileScreen(
    state: ProfileScreenState,
    onEvent: (ProfileScreenEvent) -> Unit,
) {
    ScreenScaffold(
        title = stringResource(R.string.profile)
    ) {
        when (state) {
            is ProfileScreenState.Success -> Content(
                state = state.user,
                onEvent = onEvent,
            )

            ProfileScreenState.Loading -> LoadingLayout()
        }
    }
}

@Composable
private fun Content(
    state: UserUiState,
    onEvent: (ProfileScreenEvent) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MyFarmerTheme.paddings.medium)
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.medium)
        ) {
            ImageView(
                modifier = Modifier
                    .widthIn(max = 200.dp)
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(CircleShape),
                imageResource = state.profilePicture,
                contentScale = ContentScale.Crop,
            )

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = state.name,
                style = MyFarmerTheme.typography.titleLarge,
            )

            ContactInfoSection(
                contactInfo = state.contactInfo,
                showErrorMessage = { onEvent(ProfileScreenEvent.OnShowErrorMessage(it)) }
            )
        }

        BottomButtons(
            modifier = Modifier.align(Alignment.BottomCenter),
            onEvent = onEvent,
        )
    }
}

@Composable
private fun BottomButtons(
    modifier: Modifier,
    onEvent: (ProfileScreenEvent) -> Unit,
) {
    Column(
        modifier = modifier.padding(MyFarmerTheme.paddings.bottomButtons),
        verticalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.extraSmall),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onEvent(ProfileScreenEvent.OnEditProfileClick) },
            colors = MyFarmerTheme.buttonColors.primary,
        ) {
            Text(text = stringResource(R.string.edit_profile))
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onEvent(ProfileScreenEvent.OnLogoutClick) },
            colors = MyFarmerTheme.buttonColors.error,
        ) {
            Text(text = stringResource(R.string.logout))
        }
    }
}
