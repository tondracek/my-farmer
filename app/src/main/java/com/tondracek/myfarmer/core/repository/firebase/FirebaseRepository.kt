package com.tondracek.myfarmer.core.repository.firebase

import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.snapshots
import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.core.repository.Repository
import com.tondracek.myfarmer.core.repository.request.AscendingSort
import com.tondracek.myfarmer.core.repository.request.DescendingSort
import com.tondracek.myfarmer.core.repository.request.FilterEq
import com.tondracek.myfarmer.core.repository.request.FilterGt
import com.tondracek.myfarmer.core.repository.request.FilterGte
import com.tondracek.myfarmer.core.repository.request.FilterIn
import com.tondracek.myfarmer.core.repository.request.FilterLt
import com.tondracek.myfarmer.core.repository.request.FilterLte
import com.tondracek.myfarmer.core.repository.request.RepositoryRequest
import com.tondracek.myfarmer.core.repository.request.RequestFilter
import com.tondracek.myfarmer.core.repository.request.RequestSort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.UUID

abstract class FirebaseRepository<Model, Entity : FirebaseEntity>(
    val entityClass: Class<Entity>,
) : Repository<Model> {

    val collectionName: String = entityClass.getAnnotation(FirestoreCollection::class.java)?.name
        ?: error("Class ${entityClass.simpleName} must be annotated with @FirestoreCollection")

    internal abstract val mapper: EntityMapper<Model, Entity>

    private val db = Firebase.firestore

    override suspend fun create(item: Model) {
        val entity: Entity = mapper.toEntity(item)

        db.collection(collectionName)
            .document(entity.id)
            .set(entity)
            .await()
    }

    override suspend fun update(item: Model): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: UUID): Boolean {
        TODO("Not yet implemented")
    }

    override fun getByID(id: UUID): Flow<Model?> {
        TODO("Not yet implemented")
    }

    override fun get(request: RepositoryRequest): Flow<List<Model>> {
        val query: Query = db.collection(collectionName)
            .applyFilters(request.filters)
            .applySorts(request.sorts)

        return query.snapshots().map {
            it.documents.mapNotNull { doc ->
                doc.toObject(entityClass)
                    ?.let { entity -> mapper.toModel(entity) }
            }
        }
    }

    private fun Query.applyFilters(filters: List<RequestFilter>): Query =
        filters.fold(this) { query, filter ->
            when (filter) {
                is FilterEq<*, *> ->
                    query.whereEqualTo(filter.field.name, filter.value)

                is FilterIn<*, *> ->
                    query.whereIn(filter.field.name, filter.values)

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

    private fun Query.applySorts(sorts: List<RequestSort>): Query =
        sorts.fold(this) { query, sort ->
            when (sort) {
                is AscendingSort<*> -> query.orderBy(sort.field.name, Query.Direction.ASCENDING)
                    .also {
                        println("xxx: ${sort.field}")
                    }

                is DescendingSort<*> -> query.orderBy(sort.field.name, Query.Direction.DESCENDING)
            }
        }
}