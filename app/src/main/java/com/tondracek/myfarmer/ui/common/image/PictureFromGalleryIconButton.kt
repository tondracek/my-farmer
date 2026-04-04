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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.image.model.ImageResource
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun PictureFromGalleryIconButton(
    modifier: Modifier = Modifier,
    useLabel: Boolean = false,
    onPictureSelected: (ImageResource) -> Unit,
) {
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                onPictureSelected(ImageResource(uri = uri))
            }
        }
    )

    Content(
        modifier = modifier,
        useLabel = useLabel,
        onClick = { galleryLauncher.launch("image/*") }
    )
}

@Composable
fun PicturesFromGalleryIconButton(
    modifier: Modifier = Modifier,
    useLabel: Boolean = false,
    onPicturesSelected: (List<ImageResource>) -> Unit,
) {
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uris ->
            val images = uris.map { ImageResource(uri = it) }
            onPicturesSelected(images)
        }
    )

    Content(
        modifier = modifier,
        useLabel = useLabel,
        onClick = { galleryLauncher.launch("image/*") }
    )
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    useLabel: Boolean,
    onClick: () -> Unit,
) {
    when (useLabel) {
        true -> Button(
            modifier = modifier,
            colors = MyFarmerTheme.buttonColors.primary,
            onClick = onClick,
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
            onClick = onClick,
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
