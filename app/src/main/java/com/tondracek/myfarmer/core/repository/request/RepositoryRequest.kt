package com.tondracek.myfarmer.core.repository.request

data class RepositoryRequest(
    val filters: List<RequestFilter> = emptyList(),
    val sorts: List<RequestSort> = emptyList(),
    val limit: Int? = null,
    val offset: Int? = null
) {
    class Builder {
        private val filters = mutableListOf<RequestFilter>()
        private val sorts = mutableListOf<RequestSort>()
        private var limit: Int? = null
        private var offset: Int? = null

        fun addFilter(filter: RequestFilter) = apply { filters.add(filter) }
        fun addFilters(vararg filters: RequestFilter?) =
            apply { this.filters.addAll(filters.filterNotNull()) }

        fun addSort(sort: RequestSort) = apply { sorts.add(sort) }
        fun addSorts(vararg sorts: RequestSort) = apply { this.sorts.addAll(sorts) }

        fun setLimit(limit: Int) = apply { this.limit = limit }

        fun setOffset(offset: Int) = apply { this.offset = offset }

        fun build() = RepositoryRequest(
            filters = filters,
            sorts = sorts,
            limit = limit,
            offset = offset
        )
    }
}