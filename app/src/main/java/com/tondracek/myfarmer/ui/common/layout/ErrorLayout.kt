package com.tondracek.myfarmer.ui.common.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tondracek.myfarmer.core.usecaseresult.UCResult

@Composable
fun ErrorLayout(modifier: Modifier = Modifier, text: String) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text)
    }
}

@Composable
fun ErrorLayout(modifier: Modifier = Modifier, error: UCResult.Failure) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = error.userError)
    }
}