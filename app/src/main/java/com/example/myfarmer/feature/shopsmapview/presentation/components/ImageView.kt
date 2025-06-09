package com.example.myfarmer.feature.shopsmapview.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import com.example.myfarmer.R
import com.example.myfarmer.shared.domain.ImageResource

@Composable
fun ImageView(
    modifier: Modifier = Modifier,
    imageResource: ImageResource?
) {
    var openImageDialog by remember { mutableStateOf(false) }

    AsyncImage(
        modifier = modifier.clickable { openImageDialog = true },
        model = imageResource?.uri,
        contentDescription = null,
        placeholder = painterResource(R.drawable.ic_launcher_foreground)
    )

    if (openImageDialog) {
        Dialog(onDismissRequest = { openImageDialog = false }) {
            AsyncImage(
                model = imageResource?.uri,
                contentDescription = null,
                placeholder = painterResource(R.drawable.ic_launcher_foreground)
            )
        }
    }
}

@Composable
@Preview
fun ImageViewPreview() {
    ImageView(
        imageResource = ImageResource("https://picsum.photos/400/300")
    )
}

@Composable
@Preview
fun ImageViewPreviewNull() {
    ImageView(
        imageResource = ImageResource(null)
    )
}
