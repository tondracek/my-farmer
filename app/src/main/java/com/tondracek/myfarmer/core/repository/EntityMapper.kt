package com.tondracek.myfarmer.core.repository

import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntity
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntityId
import java.util.UUID

interface EntityMapper<Model, ModelId, Entity : RepositoryEntity<EntityId>, EntityId> {

    fun toEntity(model: Model): Entity

    fun toModel(entity: Entity): Model

    fun toModelId(entityId: EntityId): ModelId

    fun toEntityId(modelId: ModelId): EntityId

    interface UUIDtoFirestore<Model, Entity : FirestoreEntity> :
        EntityMapper<Model, UUID, Entity, FirestoreEntityId> {

        override fun toModelId(entityId: String): UUID = UUID.fromString(entityId)

        override fun toEntityId(modelId: UUID): String = modelId.toString()
    }

    interface UUIDtoString<Model, Entity : RepositoryEntity<String>> :
        EntityMapper<Model, UUID, Entity, String> {

        override fun toModelId(entityId: String): UUID = UUID.fromString(entityId)

        override fun toEntityId(modelId: UUID): String = modelId.toString()
    }

    interface StringToString<Model, Entity : RepositoryEntity<String>> :
        EntityMapper<Model, String, Entity, String> {

        override fun toModelId(entityId: String): String = entityId

        override fun toEntityId(modelId: String): String = modelId
    }
}