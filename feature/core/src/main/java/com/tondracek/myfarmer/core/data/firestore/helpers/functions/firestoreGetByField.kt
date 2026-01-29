package com.tondracek.myfarmer.core.data.firestore.helpers.functions

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.snapshots
import com.tondracek.myfarmer.core.data.firestore.FirestoreEntity
import com.tondracek.myfarmer.core.data.firestore.helpers.mapToEntities
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

fun <Entity : FirestoreEntity, V> firestoreGetByField(
    collection: CollectionReference,
    entityClass: KClass<Entity>,
    field: FieldPath,
    value: V,
): Flow<List<Entity>> = collection
    .whereEqualTo(field, value)
    .snapshots()
    .mapToEntities(entityClass)