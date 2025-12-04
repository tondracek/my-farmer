package com.tondracek.myfarmer.core.repository.fake

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

object FakeQueryBuilder {

    fun <Entity> Collection<Entity>.applyFilters(filters: List<RequestFilter>): Collection<Entity> =
        filters.fold(this) { list, filter ->
            when (filter) {
                is FilterEq<*, *> ->
                    list.filter { entity ->
                        @Suppress("UNCHECKED_CAST")
                        val f = filter as FilterEq<Entity, Any?>
                        val fieldValue = f.field.get(entity)
                        fieldValue == f.value
                    }

                is FilterIn<*, *> ->
                    list.filter { entity ->
                        @Suppress("UNCHECKED_CAST")
                        val f = filter as FilterIn<Entity, Any?>
                        val fieldValue = f.field.get(entity)
                        fieldValue in f.values
                    }

                is FilterGt<*, *> ->
                    list.filter { entity ->
                        @Suppress("UNCHECKED_CAST")
                        val f = filter as FilterGt<Entity, Comparable<Any>>
                        val fieldValue = f.field.get(entity)
                        fieldValue > f.value
                    }

                is FilterGte<*, *> ->
                    list.filter { entity ->
                        @Suppress("UNCHECKED_CAST")
                        val f = filter as FilterGte<Entity, Comparable<Any>>
                        val fieldValue = f.field.get(entity)
                        fieldValue >= f.value
                    }

                is FilterLt<*, *> ->
                    list.filter { entity ->
                        @Suppress("UNCHECKED_CAST")
                        val f = filter as FilterLt<Entity, Comparable<Any>>
                        val fieldValue = f.field.get(entity)
                        fieldValue < f.value
                    }

                is FilterLte<*, *> ->
                    list.filter { entity ->
                        @Suppress("UNCHECKED_CAST")
                        val f = filter as FilterLte<Entity, Comparable<Any>>
                        val fieldValue = f.field.get(entity)
                        fieldValue <= f.value
                    }
            }
        }

    fun <Entity> Collection<Entity>.applySorts(sorts: List<RequestSort>): Collection<Entity> =
        sorts.asReversed().fold(this) { list: Collection<Entity>, sort: RequestSort ->
            when (sort) {
                is AscendingSort<*, *> ->
                    list.sortedBy { entity ->
                        @Suppress("UNCHECKED_CAST")
                        val s = sort as AscendingSort<Entity, Comparable<Any>>
                        s.field.get(entity)
                    }

                is DescendingSort<*, *> ->
                    list.sortedByDescending { entity ->
                        @Suppress("UNCHECKED_CAST")
                        val s = sort as DescendingSort<Entity, Comparable<Any>>
                        s.field.get(entity)
                    }
            }
        }

    fun <Entity> Collection<Entity>.applyLimit(limit: Int?): Collection<Entity> =
        limit?.let { this.take(it) } ?: this

    fun <Entity> Collection<Entity>.applyOffset(offset: Int?): Collection<Entity> =
        offset?.let { this.drop(it) } ?: this
}