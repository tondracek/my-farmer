package com.tondracek.myfarmer.ui.core.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import timber.log.Timber

@Composable
fun <Effect> BaseViewModel<Effect>.CollectEffects(
    onEffect: (Effect) -> Unit,
) {
    LaunchedEffect(Unit) {
        effects.collect { effect ->
            Timber.d("Collecting effect: $effect")
            onEffect(effect)
        }
    }
}