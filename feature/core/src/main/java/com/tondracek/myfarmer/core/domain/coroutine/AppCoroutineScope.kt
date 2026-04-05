package com.tondracek.myfarmer.core.domain.coroutine

import kotlinx.coroutines.CoroutineScope

interface AppCoroutineScope {
    val scope: CoroutineScope
}