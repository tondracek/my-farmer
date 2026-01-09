package com.tondracek.myfarmer.ui.editprofilescreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.ui.common.image.ImageView
import com.tondracek.myfarmer.ui.common.image.PictureFromCameraIconButton
import com.tondracek.myfarmer.ui.common.image.PictureFromGalleryIconButton
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.editprofilescreen.EditProfileScreenState

@Composable
fun ProfilePictureEdit(
    state: EditProfileScreenState.Success,
    onProfilePictureChange: (ImageResource) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MyFarmerTheme.paddings.medium),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
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
        Column {
            PictureFromGalleryIconButton { onProfilePictureChange(it) }
            PictureFromCameraIconButton { onProfilePictureChange(it) }
        }
    }
}

