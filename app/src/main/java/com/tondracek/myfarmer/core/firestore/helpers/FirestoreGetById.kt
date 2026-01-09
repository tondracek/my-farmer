package com.tondracek.myfarmer.core.firestore.helpers

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.snapshots
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntity
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntityId
import com.tondracek.myfarmer.core.repository.firestore.mapToEntity
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

fun <T : FirestoreEntity> firestoreGetById(
    collection: CollectionReference,
    id: FirestoreEntityId,
    entityClass: KClass<T>
): Flow<T?> =
    collection
        .document(id)
        .snapshots()
        .mapToEntity(entityClass)
