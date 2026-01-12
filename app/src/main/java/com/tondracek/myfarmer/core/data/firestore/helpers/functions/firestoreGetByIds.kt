package com.tondracek.myfarmer.core.data.firestore.helpers.functions

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import com.tondracek.myfarmer.core.domain.repository.firestore.FirestoreEntity
import com.tondracek.myfarmer.core.domain.repository.firestore.FirestoreEntityId
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

fun <T : FirestoreEntity> firestoreGetByIds(
    collection: CollectionReference,
    ids: List<FirestoreEntityId>,
    entityClass: KClass<T>
): Flow<List<T>> =
    firestoreGetByFieldValues(
        collection = collection,
        entityClass = entityClass,
        field = FieldPath.of(FirestoreEntity::id.name),
        values = ids.map { it }
    )
