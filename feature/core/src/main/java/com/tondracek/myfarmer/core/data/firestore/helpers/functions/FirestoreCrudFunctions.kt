package com.tondracek.myfarmer.core.data.firestore.helpers.functions

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.snapshots
import com.tondracek.myfarmer.core.data.firestore.FirestoreEntity
import com.tondracek.myfarmer.core.data.firestore.FirestoreEntityId
import com.tondracek.myfarmer.core.data.firestore.helpers.mapToEntities
import com.tondracek.myfarmer.core.data.firestore.helpers.mapToEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlin.reflect.KClass

suspend fun <T : FirestoreEntity> firestoreCreate(
    collection: CollectionReference,
    item: T,
): FirestoreEntityId =
    collection.document(item.id)
        .set(item)
        .await()
        .let { item.id }

suspend fun <T : FirestoreEntity> firestoreUpdate(
    collection: CollectionReference,
    item: T,
) {
    collection
        .document(item.id)
        .set(item)
        .await()
}

suspend fun firestoreDeleteById(
    collection: CollectionReference,
    id: FirestoreEntityId,
) {
    collection
        .document(id)
        .delete()
        .await()
}

fun <T : FirestoreEntity> firestoreGetById(
    collection: CollectionReference,
    id: FirestoreEntityId,
    entityClass: KClass<out T>,
): Flow<T?> =
    collection
        .document(id)
        .snapshots()
        .mapToEntity(entityClass)

fun <T : FirestoreEntity> firestoreGetAll(
    collection: CollectionReference,
    entityClass: KClass<T>,
): Flow<List<T>> =
    collection
        .snapshots()
        .mapToEntities(entityClass)
