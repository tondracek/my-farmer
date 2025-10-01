package com.tondracek.myfarmer.core.repository.firebase

import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.core.repository.Repository
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
import kotlinx.coroutines.flow.flow
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

    override fun get(request: RepositoryRequest): Flow<List<Model>> = flow {
        val query: Query = db.collection(collectionName)

        query.applyFilters(request.filters)
        query.applySorts(request.sorts)

        val snapshot = query.get().await()
        val entities = snapshot.toObjects(entityClass)
        val models = entities.map { entity -> mapper.toModel(entity) }
        emit(models)
    }

    private fun Query.applyFilters(filters: List<RequestFilter>): Query = apply {
        for (filter in filters) {
            when (filter) {
                is FilterEq<*, *> ->
                    this.whereEqualTo(filter.field.name, filter.value)

                is FilterIn<*, *> ->
                    this.whereIn(filter.field.name, filter.values)

                is FilterGt<*, *> ->
                    this.whereGreaterThan(filter.field.name, filter.value as Any)

                is FilterGte<*, *> ->
                    this.whereGreaterThanOrEqualTo(filter.field.name, filter.value as Any)

                is FilterLt<*, *> ->
                    this.whereLessThan(filter.field.name, filter.value as Any)

                is FilterLte<*, *> ->
                    this.whereLessThanOrEqualTo(filter.field.name, filter.value as Any)
            }
        }
    }

    private fun Query.applySorts(sorts: List<RequestSort>): Query = apply {
    }
}