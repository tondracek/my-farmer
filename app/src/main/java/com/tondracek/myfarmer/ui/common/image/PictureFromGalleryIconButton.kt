package com.tondracek.myfarmer.ui.common.image

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun PictureFromGalleryIconButton(
    modifier: Modifier = Modifier,
    onPictureSelected: (ImageResource) -> Unit
) {
    var newPicture: ImageResource by remember {
        mutableStateOf(ImageResource.EMPTY)
    }
    LaunchedEffect(newPicture.uri) {
        if (newPicture != ImageResource.EMPTY)
            onPictureSelected(newPicture)
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let { newPicture = ImageResource(it) }
        }
    )

    IconButton(
        modifier = modifier,
        colors = MyFarmerTheme.iconButtonColors.primary,
        onClick = { galleryLauncher.launch("image/*") }
    ) {
        Icon(
            imageVector = Icons.Default.PhotoLibrary,
            contentDescription = null,
        )
    }
}
