package com.tondracek.myfarmer.core.repository.request

import kotlin.reflect.KProperty1

sealed interface RequestSort

/**
 * 1,2,3 or A,B,C
 */
data class AscendingSort<T>(
    val field: KProperty1<T, *>
) : RequestSort

/**
 * 3,2,1 or C,B,A
 */
data class DescendingSort<T>(
    val field: KProperty1<T, *>
) : RequestSort