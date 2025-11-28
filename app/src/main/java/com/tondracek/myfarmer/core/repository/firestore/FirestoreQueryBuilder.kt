package com.tondracek.myfarmer.core.repository.firestore

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.tondracek.myfarmer.core.repository.request.AscendingSort
import com.tondracek.myfarmer.core.repository.request.DescendingSort
import com.tondracek.myfarmer.core.repository.request.FilterEq
import com.tondracek.myfarmer.core.repository.request.FilterGt
import com.tondracek.myfarmer.core.repository.request.FilterGte
import com.tondracek.myfarmer.core.repository.request.FilterIn
import com.tondracek.myfarmer.core.repository.request.FilterLt
import com.tondracek.myfarmer.core.repository.request.FilterLte
import com.tondracek.myfarmer.core.repository.request.RequestFilter
import com.tondracek.myfarmer.core.repository.request.RequestSort

object FirestoreQueryBuilder {

    fun Query.applyFilters(filters: List<RequestFilter>): Query =
        filters.fold(this) { query, filter ->
            when (filter) {
                is FilterEq<*, *> ->
                    query.whereEqualTo(filter.field.name, filter.value)

                is FilterIn<*, *> -> if (filter.values.isNotEmpty())
                    query.whereIn(filter.field.name, filter.values)
                else query

                is FilterGt<*, *> ->
                    query.whereGreaterThan(filter.field.name, filter.value as Any)

                is FilterGte<*, *> ->
                    query.whereGreaterThanOrEqualTo(filter.field.name, filter.value as Any)

                is FilterLt<*, *> ->
                    query.whereLessThan(filter.field.name, filter.value as Any)

                is FilterLte<*, *> ->
                    query.whereLessThanOrEqualTo(filter.field.name, filter.value as Any)
            }
        }

    fun Query.applySorts(sorts: List<RequestSort>): Query =
        sorts.fold(this) { query, sort ->
            when (sort) {
                is AscendingSort<*> -> query.orderBy(sort.field.name, Query.Direction.ASCENDING)
                is DescendingSort<*> -> query.orderBy(sort.field.name, Query.Direction.DESCENDING)
            }
        }

    fun Query.applyLimit(limit: Int?): Query =
        limit?.let { this.limit(it.toLong()) } ?: this

    fun Query.applyOffset(startAfter: DocumentSnapshot?): Query =
        startAfter?.let { this.startAfter(startAfter) } ?: this
}