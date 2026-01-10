package com.tondracek.myfarmer.core.firestore.helpers.functions

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.tondracek.myfarmer.core.firestore.helpers.mapToEntities
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntity
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntityId
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

fun <Entity : FirestoreEntity> firestoreGetPaginated(
    collection: CollectionReference,
    entityClass: KClass<Entity>,
    limit: Int?,
    after: FirestoreEntityId?,
): Flow<List<Entity>> = collection
    .orderBy(FieldPath.documentId())
    .startAfterNullable(after)
    .limitNullable(limit)
    .snapshots()
    .mapToEntities(entityClass)

fun <Entity : FirestoreEntity, V> firestoreGetPaginatedByField(
    collection: CollectionReference,
    entityClass: KClass<Entity>,
    field: FieldPath,
    value: V,
    limit: Int?,
    after: FirestoreEntityId?,
): Flow<List<Entity>> = collection
    .whereEqualTo(field, value)
    .orderBy(FieldPath.documentId())
    .startAfterNullable(after)
    .limitNullable(limit)
    .snapshots()
    .mapToEntities(entityClass)


private fun Query.startAfterNullable(lastReviewId: FirestoreEntityId?): Query =
    lastReviewId?.let { id -> this.startAfter(id) } ?: this

private fun Query.limitNullable(limit: Int?): Query =
    limit?.let { this.limit(it.toLong()) } ?: this