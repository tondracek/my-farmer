package com.tondracek.myfarmer.core.data.firestore.helpers.functions

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.snapshots
import com.tondracek.myfarmer.core.data.firestore.FirestoreEntity
import com.tondracek.myfarmer.core.data.firestore.helpers.mapToEntities
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlin.reflect.KClass

private const val FIRESTORE_WHERE_IN_LIMIT = 10

fun <Entity : FirestoreEntity, V> firestoreGetByFieldValues(
    collection: CollectionReference,
    entityClass: KClass<Entity>,
    field: FieldPath,
    values: List<V>,
): Flow<List<Entity>> {
    if (values.isEmpty()) return flowOf(emptyList())

    val chunks = values.chunked(FIRESTORE_WHERE_IN_LIMIT)

    val flows = chunks.map { chunk ->
        collection
            .whereIn(field, chunk)
            .snapshots()
            .mapToEntities(entityClass)
    }

    return combine(flows) { results ->
        results.toList().flatten()
    }
}