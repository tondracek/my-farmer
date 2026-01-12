package com.tondracek.myfarmer.core.firestore.helpers

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.snapshots
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlin.reflect.KClass

fun <Entity : FirestoreEntity> DocumentSnapshot.toObjectWithId(entityClass: KClass<Entity>): Entity? {
    return this.toObject(entityClass.java)
        ?.also { it.id = this.id }
}

fun <Entity : FirestoreEntity> QuerySnapshot.toObjectsWithId(entityClass: KClass<Entity>): List<Entity> {
    return this.documents.mapNotNull { doc ->
        doc.toObjectWithId(entityClass)
    }
}

suspend fun <Entity : FirestoreEntity> Query.getEntities(
    entityClass: KClass<Entity>,
) =
    this.get().await().toObjectsWithId(entityClass)


fun <Entity : FirestoreEntity> Query.getEntitiesFlow(
    entityClass: KClass<Entity>,
) =
    this.snapshots().map { querySnapshot ->
        querySnapshot.toObjectsWithId(entityClass)
    }


fun <Entity : FirestoreEntity> Flow<DocumentSnapshot>.mapToEntity(entityClass: KClass<Entity>): Flow<Entity?> =
    this.map { it.toObjectWithId(entityClass) }

fun <Entity : FirestoreEntity> Flow<QuerySnapshot>.mapToEntities(entityClass: KClass<Entity>): Flow<List<Entity>> =
    this.map {
        it.documents.mapNotNull { doc -> doc.toObjectWithId(entityClass) }
    }

fun <Entity : FirestoreEntity, Model> Flow<DocumentSnapshot>.mapToDomain(
    entityClass: KClass<Entity>,
    toDomain: (Entity) -> Model,
): Flow<Model?> =
    this.mapToEntity(entityClass)
        .map { entity -> entity?.let { toDomain(it) } }

fun <Entity : FirestoreEntity, Model> Flow<QuerySnapshot>.mapToDomains(
    entityClass: KClass<Entity>,
    toDomain: (Entity) -> Model,
): Flow<List<Model>> =
    this.mapToEntities(entityClass)
        .map { entities -> entities.map { entity -> toDomain(entity) } }
