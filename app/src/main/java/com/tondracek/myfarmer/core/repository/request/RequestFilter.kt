package com.tondracek.myfarmer.core.repository.request

import kotlin.reflect.KProperty1

sealed interface RequestFilter

data class FilterEq<T, R>(
    val field: KProperty1<T, R>,
    val value: R
) : RequestFilter

data class FilterIn<T, R>(
    val field: KProperty1<T, R>,
    val values: List<R>
) : RequestFilter

data class FilterLt<T, R>(
    val field: KProperty1<T, R>,
    val value: R
) : RequestFilter

data class FilterLte<T, R>(
    val field: KProperty1<T, R>,
    val value: R
) : RequestFilter

data class FilterGt<T, R>(
    val field: KProperty1<T, R>,
    val value: R
) : RequestFilter

data class FilterGte<T, R>(
    val field: KProperty1<T, R>,
    val value: R
) : RequestFilter
