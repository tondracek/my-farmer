package com.tondracek.myfarmer.ui.core.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember

@Composable
fun RecompositionLogger(tag: String) {
    val count = remember { mutableIntStateOf(0) }

    SideEffect {
        count.intValue++
        println("🔁 Recomposition [$tag]: ${count.intValue}")
    }
}