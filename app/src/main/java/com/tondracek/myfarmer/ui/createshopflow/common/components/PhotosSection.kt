package com.tondracek.myfarmer.ui.createshopflow.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.ui.common.image.ImageView
import com.tondracek.myfarmer.ui.common.image.PictureFromCameraIconButton
import com.tondracek.myfarmer.ui.common.image.PictureFromGalleryIconButton
import com.tondracek.myfarmer.ui.core.preview.MyFarmerPreview
import com.tondracek.myfarmer.ui.core.preview.PreviewDark
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme

@Composable
fun PhotosSection(
    images: List<ImageResource>,
    onAddImage: (ImageResource) -> Unit,
    onMoveImageLeft: (Int) -> Unit,
    onMoveImageRight: (Int) -> Unit,
    onRemoveImage: (ImageResource) -> Unit
) {
    Card(colors = MyFarmerTheme.cardColors.base) {
        Column(
            modifier = Modifier.padding(MyFarmerTheme.paddings.medium),
            verticalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.shop_photos),
                style = MyFarmerTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            PhotoList(
                images = images,
                onMoveImageLeft = onMoveImageLeft,
                onMoveImageRight = onMoveImageRight,
                onRemoveImage = onRemoveImage,
            )

            AddPhotoButtons { onAddImage(it) }
        }
    }
}

@Composable
private fun AddPhotoButtons(
    modifier: Modifier = Modifier,
    onPhotoAdded: (ImageResource) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PictureFromGalleryIconButton(useLabel = true) {
            onPhotoAdded(it)
        }
        PictureFromCameraIconButton(useLabel = true) {
            onPhotoAdded(it)
        }
    }
}

@Composable
private fun PhotoList(
    images: List<ImageResource>,
    onMoveImageLeft: (Int) -> Unit,
    onMoveImageRight: (Int) -> Unit,
    onRemoveImage: (ImageResource) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(MyFarmerTheme.paddings.medium)
    ) {
        itemsIndexed(images) { i, image ->
            PhotoItem(
                image = image,
                onMoveLeftClick = { onMoveImageLeft(i) },
                onMoveRightClick = { onMoveImageRight(i) },
                onRemoveClick = { onRemoveImage(image) },
            )
        }
    }
}

@Composable
private fun PhotoItem(
    image: ImageResource,
    onMoveLeftClick: () -> Unit,
    onMoveRightClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(MyFarmerTheme.paddings.medium),
        colors = MyFarmerTheme.cardColors.secondary
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            ImageView(
                modifier = Modifier
                    .height(120.dp)
                    .padding(MyFarmerTheme.paddings.smallMedium),
                imageResource = image,
            )

            Row {
                IconButton(onClick = onRemoveClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove photo"
                    )
                }

                IconButton(onClick = onMoveLeftClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Move photo left"
                    )
                }

                IconButton(onClick = onMoveRightClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Move photo right"
                    )
                }
            }
        }
    }
}


@PreviewDark
@Composable
private fun PhotosSectionPreview() {
    MyFarmerPreview {
        PhotosSection(
            images = listOf(
                ImageResource("https://picsum.photos/200"),
                ImageResource("https://picsum.photos/201"),
                ImageResource("https://picsum.photos/202"),
            ),
            onAddImage = {},
            onMoveImageLeft = {},
            onMoveImageRight = {},
            onRemoveImage = {},
        )
    }
}