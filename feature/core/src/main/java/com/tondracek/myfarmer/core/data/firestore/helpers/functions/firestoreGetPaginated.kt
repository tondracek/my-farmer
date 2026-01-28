package com.tondracek.myfarmer.core.data.firestore.helpers.functions

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Query
import com.tondracek.myfarmer.core.data.firestore.FirestoreEntity
import com.tondracek.myfarmer.core.data.firestore.FirestoreEntityId
import com.tondracek.myfarmer.core.data.firestore.helpers.getEntities
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

private fun Query.startAfterNullable(lastReviewId: FirestoreEntityId?): Query =
    lastReviewId?.let { id -> this.startAfter(id) } ?: this

private fun Query.limitNullable(limit: Int?): Query =
    limit?.let { this.limit(it.toLong()) } ?: this