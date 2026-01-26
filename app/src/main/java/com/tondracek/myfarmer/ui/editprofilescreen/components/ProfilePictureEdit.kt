package com.tondracek.myfarmer.ui.editprofilescreen.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.ui.common.image.ImageView
import com.tondracek.myfarmer.ui.common.image.PictureFromCameraIconButton
import com.tondracek.myfarmer.ui.common.image.PictureFromGalleryIconButton
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun ProfilePictureEdit(
    profilePicture: ImageResource,
    onProfilePictureChange: (ImageResource) -> Unit,
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MyFarmerTheme.paddings.medium)
    ) {
        val (image, galleryButton, cameraButton) = createRefs()

        ImageView(
            modifier = Modifier
                .widthIn(max = 200.dp)
                .aspectRatio(1f)
                .clip(CircleShape)
                .constrainAs(image) {
                    centerHorizontallyTo(parent)
                    centerVerticallyTo(parent)
                },
            imageResource = profilePicture,
            contentScale = ContentScale.Crop,
        )

        PictureFromGalleryIconButton(
            modifier = Modifier.constrainAs(galleryButton) {
                start.linkTo(image.end, margin = 16.dp)
                top.linkTo(image.top)
            }
        ) { onProfilePictureChange(it) }

        PictureFromCameraIconButton(
            modifier = Modifier.constrainAs(cameraButton) {
                start.linkTo(image.end, margin = 16.dp)
                top.linkTo(galleryButton.bottom, margin = 8.dp)
            }
        ) { onProfilePictureChange(it) }
    }
}
