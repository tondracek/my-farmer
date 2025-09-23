package com.tondracek.myfarmer.repository.request

sealed interface RequestFilter

data class FilterEq<T>(
    val field: String,
    val value: T
) : RequestFilter

data class FilterIn<T>(
    val field: String,
    val values: List<T>
) : RequestFilter

data class FilterLt<T : Comparable<T>>(
    val field: String,
    val value: T
) : RequestFilter

data class FilterLte<T : Comparable<T>>(
    val field: String,
    val value: T
) : RequestFilter

data class FilterGt<T : Comparable<T>>(
    val field: String,
    val value: T
) : RequestFilter

data class FilterGte<T : Comparable<T>>(
    val field: String,
    val value: T
) : RequestFilter
