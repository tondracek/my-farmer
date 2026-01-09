package com.tondracek.myfarmer.ui.common.image

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.FileProvider
import com.tondracek.myfarmer.BuildConfig
import com.tondracek.myfarmer.R
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.ui.core.theme.myfarmertheme.MyFarmerTheme
import java.io.File
import java.util.UUID

@Composable
fun PictureFromCameraIconButton(
    modifier: Modifier = Modifier,
    useLabel: Boolean = false,
    onPictureTaken: (ImageResource) -> Unit
) {
    val context = LocalContext.current

    var newPicture: ImageResource by remember {
        mutableStateOf(ImageResource.EMPTY)
    }
    LaunchedEffect(newPicture.uri) {
        if (newPicture != ImageResource.EMPTY)
            onPictureTaken(newPicture)
    }

    var cameraLauncherUri by remember { mutableStateOf(Uri.EMPTY) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { saved ->
            if (saved) newPicture = ImageResource(uri = cameraLauncherUri.toString())
        }
    )


    when (useLabel) {
        true -> Button(
            modifier = modifier,
            colors = MyFarmerTheme.buttonColors.primary,
            onClick = {
                cameraLauncherUri = context.createTempPictureUri()
                cameraLauncher.launch(cameraLauncherUri)
            }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = null,
                )
                Text(
                    modifier = Modifier.padding(start = MyFarmerTheme.paddings.small),
                    text = stringResource(R.string.camera),
                    style = MyFarmerTheme.typography.textMedium,
                )
            }
        }

        false -> IconButton(
            modifier = modifier,
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
