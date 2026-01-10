package com.tondracek.myfarmer.core.firestore.helpers

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.reflect.KClass

fun <Entity : FirestoreEntity> DocumentSnapshot.toObjectWithId(entityClass: KClass<Entity>): Entity? {
    return this.toObject(entityClass.java)
        ?.also { it.id = this.id }
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
