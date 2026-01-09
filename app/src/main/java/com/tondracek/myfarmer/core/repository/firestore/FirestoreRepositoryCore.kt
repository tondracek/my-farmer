package com.tondracek.myfarmer.core.repository.firestore

import com.google.firebase.firestore.DocumentSnapshot
import com.tondracek.myfarmer.core.repository.RepositoryCore
import com.tondracek.myfarmer.core.repository.firestore.firestoreclient.FirestoreClient
import com.tondracek.myfarmer.core.repository.firestore.firestoreclient.FirestoreClientImpl
import com.tondracek.myfarmer.core.repository.request.RepositoryRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import kotlin.reflect.KClass

class FirestoreRepositoryCore<Entity : FirestoreEntity>(
    val entityClass: Class<Entity>,
    val firestore: FirestoreClient = FirestoreClientImpl()
) : RepositoryCore<Entity, FirestoreEntityId> {

    val collectionName: String =
        entityClass.getAnnotation(FirestoreCollectionName::class.java)?.name
            ?: error("Class ${entityClass.simpleName} must be annotated with @FirestoreCollection")

    override suspend fun create(entity: Entity): FirestoreEntityId {
        firestore.collection(collectionName)
            .document(entity.id)
            .set(entity)
        return entity.id
    }

    override suspend fun update(entity: Entity) {
        firestore.collection(collectionName)
            .document(entity.id)
            .set(entity)
    }

    override suspend fun delete(id: FirestoreEntityId) {
        firestore.collection(collectionName)
            .document(id)
            .delete()
    }

    override fun getById(id: FirestoreEntityId): Flow<Entity?> {
        val docRef = firestore.collection(collectionName).document(id)

        return docRef.snapshots().map { it.toObjectWithId(entityClass) }
    }

    override fun get(request: RepositoryRequest): Flow<List<Entity>> = flow {
        if (request.filters.isEmpty())
            Timber.w("Request doesn't have any filters set")

        val startAfter: DocumentSnapshot? = getStartAfter(request)

        val query = firestore.collection(collectionName)
            .applyFilters(request.filters)
            .applySorts(request.sorts)
            .applyOffset(startAfter)
            .applyLimit(request.limit)

        emitAll(
            query.snapshots().map {
                it.documents.mapNotNull { doc -> doc.toObjectWithId(entityClass) }
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

    override fun getAll(): Flow<List<Entity>> {
        val query = firestore.collection(collectionName)

        return query.snapshots().map {
            it.documents.mapNotNull { doc -> doc.toObjectWithId(entityClass) }
        }
    }
}

fun <Entity : FirestoreEntity> DocumentSnapshot.toObjectWithId(entityClass: Class<Entity>): Entity? {
    return this.toObject(entityClass)
        ?.also { it.id = this.id }
}

fun <Entity : FirestoreEntity> DocumentSnapshot.toObjectWithId(entityClass: KClass<Entity>): Entity? {
    return this.toObject(entityClass.java)
        ?.also { it.id = this.id }
}

fun <Entity : FirestoreEntity> Flow<DocumentSnapshot>.mapToEntity(entityClass: KClass<Entity>): Flow<Entity?> =
    this.map { it.toObjectWithId(entityClass) }

fun <Entity : FirestoreEntity> Flow<List<DocumentSnapshot>>.mapToEntities(entityClass: KClass<Entity>): Flow<List<Entity>> =
    this.map { list ->
        list.mapNotNull { it.toObjectWithId(entityClass) }
    }