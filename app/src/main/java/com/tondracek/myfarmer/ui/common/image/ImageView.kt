package com.tondracek.myfarmer.ui.common.image

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.common.image.model.ImageResource

@Composable
fun ImageView(
    modifier: Modifier = Modifier,
    imageResource: ImageResource,
    contentScale: ContentScale = ContentScale.Fit,
) {
    var openImageDialog by remember { mutableStateOf(false) }

    val model: Any? by produceState(imageResource.uri, imageResource.uri) {
        value = imageResource.getImageUrl()
    }

    AsyncImage(
        modifier = modifier.clickable { openImageDialog = true },
        model = model,
        contentDescription = null,
        placeholder = painterResource(R.drawable.ic_launcher_foreground),
        contentScale = contentScale,
    )

    if (openImageDialog) {
        Dialog(onDismissRequest = { openImageDialog = false }) {
            AsyncImage(
                model = model,
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
        imageResource = ImageResource.EMPTY
    )
}
