package com.tondracek.myfarmer.ui.editprofilescreen.components

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.tondracek.myfarmer.BuildConfig
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.ui.common.image.ImageView
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import com.tondracek.myfarmer.ui.editprofilescreen.EditProfileScreenState
import java.io.File
import java.util.UUID

@Composable
fun ProfilePictureEdit(
    state: EditProfileScreenState.Success,
    onProfilePictureChange: (ImageResource) -> Unit,
) {
    val context = LocalContext.current

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
            var newPicture: ImageResource by remember {
                mutableStateOf(ImageResource.EMPTY)
            }
            LaunchedEffect(newPicture.uri) {
                if (newPicture != ImageResource.EMPTY)
                    onProfilePictureChange(newPicture)
            }

            val galleryLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent(),
                onResult = { uri ->
                    uri?.let { newPicture = ImageResource(uri = it.toString()) }
                }
            )

            var cameraLauncherUri by remember { mutableStateOf(Uri.EMPTY) }
            val cameraLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.TakePicture(),
                onResult = { saved ->
                    if (saved) newPicture = ImageResource(uri = cameraLauncherUri.toString())
                }
            )

            IconButton(
                colors = MyFarmerTheme.iconButtonColors.primary,
                onClick = { galleryLauncher.launch("image/*") }
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoLibrary,
                    contentDescription = null,
                )
            }
            IconButton(
                colors = MyFarmerTheme.iconButtonColors.primary,
                onClick = {
                    cameraLauncherUri = context.createTempPictureUri()
                    cameraLauncher.launch(cameraLauncherUri)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = null,
                )
            }
        }
    }
}

fun Context.createTempPictureUri(
    provider: String = "${BuildConfig.APPLICATION_ID}.provider",
    fileName: String = "picture_${UUID.randomUUID()}",
    fileExtension: String = ".png"
): Uri {
    val tempFile = File.createTempFile(
        fileName, fileExtension, cacheDir
    ).apply {
        createNewFile()
    }

    return FileProvider.getUriForFile(applicationContext, provider, tempFile)
}
