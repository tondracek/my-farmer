package com.tondracek.myfarmer.ui.core.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(apiLevel = 34)
annotation class PreviewApi34

@Preview(apiLevel = 34, uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class PreviewApi34Dark

@Preview(apiLevel = 34, uiMode = Configuration.UI_MODE_NIGHT_NO)
annotation class PreviewApi34Light
