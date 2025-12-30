package com.tondracek.myfarmer.core.repository

import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntityId
import java.util.UUID

interface IdMapper<ModelId, EntityId> {
    fun toEntityId(modelId: ModelId): EntityId
    fun toModelId(entityId: EntityId): ModelId

    object UUIDtoFirestore : IdMapper<UUID, FirestoreEntityId> {

        override fun toModelId(entityId: String): UUID = UUID.fromString(entityId)

        override fun toEntityId(modelId: UUID): String = modelId.toString()
    }

    object UUIDtoString : IdMapper<UUID, String> {

        override fun toModelId(entityId: String): UUID = UUID.fromString(entityId)

        override fun toEntityId(modelId: UUID): String = modelId.toString()
    }

    object StringToString : IdMapper<String, String> {

        override fun toModelId(entityId: String): String = entityId

        override fun toEntityId(modelId: String): String = modelId
    }
}