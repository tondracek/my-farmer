package com.tondracek.myfarmer.ui.core.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun <Effect> BaseViewModel<Effect>.CollectEffects(
    onEffect: (Effect) -> Unit,
) {
    LaunchedEffect(Unit) {
        effects.collect { effect ->
            onEffect(effect)
        }
    }
}