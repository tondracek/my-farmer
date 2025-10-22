package com.tondracek.myfarmer.core.repository.request

import kotlin.reflect.KProperty1

sealed interface RequestFilter

data class FilterEq<T, R>(
    val field: KProperty1<T, R>,
    val value: R
) : RequestFilter

inline infix fun <T, reified R> KProperty1<T, R>.filterEq(value: R): FilterEq<T, R> =
    FilterEq(this, value)

data class FilterIn<T, R>(
    val field: KProperty1<T, R>,
    val values: List<R>
) : RequestFilter

inline infix fun <T, reified R> KProperty1<T, R>.filterIn(values: List<R>): FilterIn<T, R> =
    FilterIn(this, values)

data class FilterLt<T, R>(
    val field: KProperty1<T, R>,
    val value: R
) : RequestFilter

inline infix fun <T, reified R> KProperty1<T, R>.filterLt(value: R): FilterLt<T, R> =
    FilterLt(this, value)

data class FilterLte<T, R>(
    val field: KProperty1<T, R>,
    val value: R
) : RequestFilter

inline infix fun <T, reified R> KProperty1<T, R>.filterLte(value: R): FilterLte<T, R> =
    FilterLte(this, value)

data class FilterGt<T, R>(
    val field: KProperty1<T, R>,
    val value: R
) : RequestFilter

inline infix fun <T, reified R> KProperty1<T, R>.filterGt(value: R): FilterGt<T, R> =
    FilterGt(this, value)

data class FilterGte<T, R>(
    val field: KProperty1<T, R>,
    val value: R
) : RequestFilter

inline infix fun <T, reified R> KProperty1<T, R>.filterGte(value: R): FilterGte<T, R> =
    FilterGte(this, value)
