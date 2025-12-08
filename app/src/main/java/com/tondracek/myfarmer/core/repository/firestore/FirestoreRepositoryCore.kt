package com.tondracek.myfarmer.core.repository.firestore

import com.google.firebase.firestore.DocumentSnapshot
import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.core.repository.RepositoryCore
import com.tondracek.myfarmer.core.repository.firestore.firestoreclient.FirestoreClient
import com.tondracek.myfarmer.core.repository.request.RepositoryRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.util.UUID

class FirestoreRepositoryCore<Model, Entity : FirestoreEntity>(
    val mapper: EntityMapper<Model, Entity>,
    val entityClass: Class<Entity>,
    val firestore: FirestoreClient,
) : RepositoryCore<Model> {

    val collectionName: String =
        entityClass.getAnnotation(FirestoreCollectionName::class.java)?.name
            ?: error("Class ${entityClass.simpleName} must be annotated with @FirestoreCollection")

    override suspend fun create(item: Model): UUID {
        val entity: Entity = mapper.toEntity(item)

        firestore.collection(collectionName)
            .document(entity.id)
            .set(entity)
        return UUID.fromString(entity.id)
    }

    override suspend fun update(item: Model) {
        val entity: Entity = mapper.toEntity(item)

        firestore.collection(collectionName)
            .document(entity.id)
            .set(entity)
    }

    override suspend fun delete(id: UUID) {
        firestore.collection(collectionName)
            .document(id.toString())
            .delete()
    }

    override fun getById(id: UUID): Flow<Model?> {
        val docRef = firestore.collection(collectionName).document(id.toString())

        return docRef.snapshots()
            .map { it.toObjectWithId(entityClass) }
            .map { entity -> entity?.let { mapper.toModel(it) } }
    }

    override fun get(request: RepositoryRequest): Flow<List<Model>> = flow {
        if (request.filters.isEmpty())
            Timber.w("Request doesn't have any filters set")

        val startAfter: DocumentSnapshot? = getStartAfter(request)

        val query = firestore.collection(collectionName)
            .applyFilters(request.filters)
            .applySorts(request.sorts)
            .applyOffset(startAfter)
            .applyLimit(request.limit)

        emitAll(
            query.snapshots()
                .map {
                    it.documents
                        .mapNotNull { doc -> doc.toObjectWithId(entityClass) }
                        .map { entity -> mapper.toModel(entity) }
                }
        )
    }

    private suspend fun getStartAfter(request: RepositoryRequest): DocumentSnapshot? =
        request.offset?.let { offset ->
            firestore.collection(collectionName)
                .applyFilters(request.filters)
                .applySorts(request.sorts)
                .applyLimit(offset)
                .get()
                .documents
                .lastOrNull()
        }

    override fun getAll(): Flow<List<Model>> {
        val query = firestore.collection(collectionName)

        return query.snapshots()
            .map {
                it.documents
                    .mapNotNull { doc -> doc.toObjectWithId(entityClass) }
                    .map { entity -> mapper.toModel(entity) }
            }
    }
}

private fun <Entity : FirestoreEntity> DocumentSnapshot.toObjectWithId(entityClass: Class<Entity>): Entity? {
    return this.toObject(entityClass)
        ?.also { it.id = this.id }
}