package com.tondracek.myfarmer.core.repository.request

import kotlin.reflect.KProperty1

sealed interface RequestSort

/**
 * 1,2,3 or A,B,C
 */
data class AscendingSort<T, R : Comparable<R>>(
    val field: KProperty1<T, R>
) : RequestSort

inline fun <reified T, R : Comparable<R>> ascending(field: KProperty1<T, R>): AscendingSort<T, R> =
    AscendingSort(field)

/**
 * 3,2,1 or C,B,A
 */
data class DescendingSort<T, R : Comparable<R>>(
    val field: KProperty1<T, R>
) : RequestSort

inline fun <reified T, R : Comparable<R>> descending(field: KProperty1<T, R>): DescendingSort<T, R> =
    DescendingSort(field)
