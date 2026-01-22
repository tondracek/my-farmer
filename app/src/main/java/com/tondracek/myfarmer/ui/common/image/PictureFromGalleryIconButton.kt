package com.tondracek.myfarmer.ui.common.image

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun PictureFromGalleryIconButton(
    modifier: Modifier = Modifier,
    useLabel: Boolean = false,
    onPictureSelected: (ImageResource) -> Unit,
) {
    var newPicture: ImageResource by remember {
        mutableStateOf(ImageResource.EMPTY)
    }
    LaunchedEffect(newPicture.uri) {
        if (newPicture != ImageResource.EMPTY) {
            onPictureSelected(newPicture)
            newPicture = ImageResource.EMPTY
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> uri?.let { newPicture = ImageResource(it) } }
    )

    when (useLabel) {
        true -> Button(
            modifier = modifier,
            colors = MyFarmerTheme.buttonColors.primary,
            onClick = { galleryLauncher.launch("image/*") },
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoLibrary,
                    contentDescription = null,
                )
                Text(
                    modifier = Modifier.padding(start = MyFarmerTheme.paddings.small),
                    text = stringResource(R.string.gallery),
                    style = MyFarmerTheme.typography.textMedium,
                )
            }
        }

        false -> IconButton(
            modifier = modifier,
            colors = MyFarmerTheme.iconButtonColors.primary,
            onClick = { galleryLauncher.launch("image/*") },
        ) {
            Icon(
                imageVector = Icons.Default.PhotoLibrary,
                contentDescription = null,
            )
        }
    }


}

@Preview
@Composable
private fun PictureFromGalleryIconButtonPreview() {
    MyFarmerTheme {
        PictureFromGalleryIconButton(
            onPictureSelected = {},
            useLabel = true,
        )
    }
}

@Preview
@Composable
private fun PictureFromGalleryIconButtonIconOnlyPreview() {
    MyFarmerTheme {
        PictureFromGalleryIconButton(
            onPictureSelected = {},
            useLabel = false,
        )
    }
}
