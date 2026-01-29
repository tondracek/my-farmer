package com.tondracek.myfarmer.core.data.firestore.helpers

import com.google.firebase.firestore.CollectionReference
import com.tondracek.myfarmer.core.data.firestore.FirestoreEntity
import com.tondracek.myfarmer.core.data.firestore.FirestoreEntityId
import com.tondracek.myfarmer.core.data.firestore.helpers.functions.firestoreCreate
import com.tondracek.myfarmer.core.data.firestore.helpers.functions.firestoreDeleteById
import com.tondracek.myfarmer.core.data.firestore.helpers.functions.firestoreGetAll
import com.tondracek.myfarmer.core.data.firestore.helpers.functions.firestoreGetById
import com.tondracek.myfarmer.core.data.firestore.helpers.functions.firestoreUpdate
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

class FirestoreCrudHelper<Entity : FirestoreEntity>(
    private val collection: CollectionReference,
    private val entityClass: KClass<Entity>,
) {

    suspend fun create(item: Entity) = firestoreCreate(
        collection = collection,
        item = item,
    )

    suspend fun update(item: Entity) = firestoreUpdate(
        collection = collection,
        item = item,
    )

    suspend fun delete(id: FirestoreEntityId) = firestoreDeleteById(
        collection = collection,
        id = id,
    )

    fun getById(id: FirestoreEntityId): Flow<Entity?> =
        firestoreGetById(
            collection = collection,
            id = id,
            entityClass = entityClass,
        )

    fun getAll(): Flow<List<Entity>> = firestoreGetAll(
        collection = collection,
        entityClass = entityClass,
    )
}
