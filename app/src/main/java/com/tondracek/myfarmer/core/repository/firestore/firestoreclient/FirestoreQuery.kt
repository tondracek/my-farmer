package com.tondracek.myfarmer.core.repository.firestore.firestoreclient

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.snapshots
import com.tondracek.myfarmer.core.repository.firestore.FirestoreQueryBuilder.applyFilters
import com.tondracek.myfarmer.core.repository.firestore.FirestoreQueryBuilder.applyLimit
import com.tondracek.myfarmer.core.repository.firestore.FirestoreQueryBuilder.applyOffset
import com.tondracek.myfarmer.core.repository.firestore.FirestoreQueryBuilder.applySorts
import com.tondracek.myfarmer.core.repository.request.RequestFilter
import com.tondracek.myfarmer.core.repository.request.RequestSort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

interface FirestoreQuery {
    fun applyFilters(filters: List<RequestFilter>): FirestoreQuery
    fun applySorts(sorts: List<RequestSort>): FirestoreQuery
    fun applyOffset(startAfter: DocumentSnapshot?): FirestoreQuery
    fun applyLimit(limit: Int?): FirestoreQuery
    fun snapshots(): Flow<QuerySnapshot>
    suspend fun get(): QuerySnapshot
}

open class FirestoreQueryImpl(
    private val query: Query
) : FirestoreQuery {

    override fun applyFilters(filters: List<RequestFilter>): FirestoreQuery =
        FirestoreQueryImpl(query.applyFilters(filters))

    override fun applySorts(sorts: List<RequestSort>): FirestoreQuery =
        FirestoreQueryImpl(query.applySorts(sorts))

    override fun applyOffset(startAfter: DocumentSnapshot?): FirestoreQuery =
        FirestoreQueryImpl(query.applyOffset(startAfter))

    override fun applyLimit(limit: Int?): FirestoreQuery =
        FirestoreQueryImpl(query.applyLimit(limit))

    override fun snapshots(): Flow<QuerySnapshot> = query.snapshots()

    override suspend fun get(): QuerySnapshot = query.get().await()
}
