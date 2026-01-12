package com.tondracek.myfarmer.core.firestore.helpers.functions

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Query
import com.tondracek.myfarmer.core.firestore.helpers.getEntities
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntity
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntityId
import timber.log.Timber
import kotlin.reflect.KClass

suspend fun <Entity : FirestoreEntity> firestoreGetPaginatedById(
    collection: CollectionReference,
    entityClass: KClass<Entity>,
    limit: Int?,
    after: FirestoreEntityId?,
): List<Entity> = collection
    .orderBy(FieldPath.documentId())
    .startAfterNullable(after)
    .limitNullable(limit)
    .getEntities(entityClass)
    .also {
        Timber.d("Fetched ${it.size} entities from ${collection.path} paginated by ID. Limit=$limit, After=$after")
    }

suspend fun <Entity : FirestoreEntity, V> firestoreGetPaginatedFilteredByField(
    collection: CollectionReference,
    entityClass: KClass<Entity>,
    field: FieldPath,
    value: V,
    limit: Int?,
    after: FirestoreEntityId?,
): List<Entity> = collection
    .whereEqualTo(field, value)
    .orderBy(FieldPath.documentId())
    .startAfterNullable(after)
    .limitNullable(limit)
    .getEntities(entityClass)


private fun Query.startAfterNullable(lastReviewId: FirestoreEntityId?): Query =
    lastReviewId?.let { id -> this.startAfter(id) } ?: this

private fun Query.limitNullable(limit: Int?): Query =
    limit?.let { this.limit(it.toLong()) } ?: this