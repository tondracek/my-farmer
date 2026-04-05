package com.tondracek.myfarmer.coroutine

import com.tondracek.myfarmer.core.domain.coroutine.AppCoroutineScope
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Singleton
class AppCoroutineScopeImpl @Inject constructor() : AppCoroutineScope {
    override val scope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
}