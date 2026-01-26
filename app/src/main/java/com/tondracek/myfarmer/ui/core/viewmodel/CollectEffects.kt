package com.tondracek.myfarmer.ui.core.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@Composable
fun <Effect> BaseViewModel<Effect>.CollectEffects(
    onEffect: (Effect) -> Unit,
) {
    val viewModel: ViewModel = this
    LaunchedEffect(viewModel) {
        effects.collectLatest { effect ->
            Timber.d("Collecting effect: $effect")
            onEffect(effect)
        }
    }
}