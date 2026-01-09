package com.tondracek.myfarmer.core.firestore.helpers.functions

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.snapshots
import com.tondracek.myfarmer.core.firestore.helpers.mapToEntities
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntity
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

fun <Entity : FirestoreEntity, V> firestoreGetWhereEqualTo(
    collection: CollectionReference,
    entityClass: KClass<Entity>,
    property: KProperty1<Entity, V>,
    value: V,
): Flow<List<Entity>> = collection
    .whereEqualTo(property.name, value)
    .snapshots()
    .mapToEntities(entityClass)