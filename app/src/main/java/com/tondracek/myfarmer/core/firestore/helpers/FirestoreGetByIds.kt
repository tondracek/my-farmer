package com.tondracek.myfarmer.core.firestore.helpers

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.snapshots
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntity
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntityId
import com.tondracek.myfarmer.core.repository.firestore.toObjectWithId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.reflect.KClass

fun <T : FirestoreEntity> firestoreGetByIds(
    collection: CollectionReference,
    ids: List<FirestoreEntityId>,
    entityClass: KClass<T>
): Flow<List<T>> =
    collection
        .whereIn(FirestoreEntity::id.name, ids)
        .snapshots()
        .map { querySnapshot ->
            querySnapshot.documents.mapNotNull { documentSnapshot ->
                documentSnapshot.toObjectWithId(entityClass)
            }
        }
