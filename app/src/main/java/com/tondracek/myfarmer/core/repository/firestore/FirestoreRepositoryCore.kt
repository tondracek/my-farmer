package com.tondracek.myfarmer.core.repository.firestore

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.snapshots
import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.core.repository.RepositoryCore
import com.tondracek.myfarmer.core.repository.firestore.FirestoreQueryBuilder.applyFilters
import com.tondracek.myfarmer.core.repository.firestore.FirestoreQueryBuilder.applyLimit
import com.tondracek.myfarmer.core.repository.firestore.FirestoreQueryBuilder.applyOffset
import com.tondracek.myfarmer.core.repository.firestore.FirestoreQueryBuilder.applySorts
import com.tondracek.myfarmer.core.repository.request.RepositoryRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.util.UUID

class FirestoreRepositoryCore<Model, Entity : FirestoreEntity>(
    private val mapper: EntityMapper<Model, Entity>,
    private val entityClass: Class<Entity>,
) : RepositoryCore<Model> {

    val collectionName: String = entityClass.getAnnotation(FirestoreCollection::class.java)?.name
        ?: error("Class ${entityClass.simpleName} must be annotated with @FirestoreCollection")

    private val db = Firebase.firestore

    override suspend fun create(item: Model): UUID {
        val entity: Entity = mapper.toEntity(item)

        db.collection(collectionName)
            .document(entity.id)
            .set(entity)
            .await()
        return UUID.fromString(entity.id)
    }

    override suspend fun update(item: Model) {
        val entity: Entity = mapper.toEntity(item)

        db.collection(collectionName)
            .document(entity.id)
            .set(entity)
            .await()
    }

    override suspend fun delete(id: UUID) {
        db.collection(collectionName)
            .document(id.toString())
            .delete()
            .await()
    }

    override fun getById(id: UUID): Flow<Model?> {
        val docRef = db.collection(collectionName).document(id.toString())

        return docRef.snapshots()
            .map { it.toObject(entityClass) }
            .map { entity -> entity?.let { mapper.toModel(it) } }
    }

    override fun get(request: RepositoryRequest): Flow<List<Model>> {
        if (request.filters.isEmpty())
            Log.w("FirestoreRepositoryCore", "Request doesn't have any filters set")

        val startAfter: DocumentSnapshot? = getStartAfter(request)

        val query: Query = db.collection(collectionName)
            .applyFilters(request.filters)
            .applySorts(request.sorts)
            .applyOffset(startAfter)
            .applyLimit(request.limit)

        return query.snapshots()
            .map {
                it.documents
                    .mapNotNull { doc -> doc.toObject(entityClass) }
                    .map { entity -> mapper.toModel(entity) }
            }
    }

    private fun getStartAfter(request: RepositoryRequest): DocumentSnapshot? =
        request.offset?.let { offset ->
            runBlocking {
                db.collection(collectionName)
                    .applyFilters(request.filters)
                    .applySorts(request.sorts)
                    .limit(offset.toLong())
                    .get()
                    .await()
                    .documents
                    .lastOrNull()
            }
        }

    override fun getAll(): Flow<List<Model>> {
        val query: Query = db.collection(collectionName)

        return query.snapshots()
            .map {
                it.documents
                    .mapNotNull { doc -> doc.toObject(entityClass) }
                    .map { entity -> mapper.toModel(entity) }
            }
    }
}
