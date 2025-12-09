package com.tondracek.myfarmer.core.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.UUID

class IdMapperTest {

    // ---------------------------------------------------------------
    // UUID ↔ Firestore (String)
    // ---------------------------------------------------------------

    @Test
    fun `UUIDtoFirestore converts UUID to string and back`() {
        val uuid = UUID.randomUUID()

        val entityId = IdMapper.UUIDtoFirestore.toEntityId(uuid)
        val modelId = IdMapper.UUIDtoFirestore.toModelId(entityId)

        assertEquals(uuid.toString(), entityId)
        assertEquals(uuid, modelId)
    }

    @Test
    fun `UUIDtoFirestore throws on invalid UUID`() {
        assertThrows(IllegalArgumentException::class.java) {
            IdMapper.UUIDtoFirestore.toModelId("not-a-uuid")
        }
    }

    // ---------------------------------------------------------------
    // UUID ↔ String
    // ---------------------------------------------------------------

    @Test
    fun `UUIDtoString converts UUID to string and back`() {
        val uuid = UUID.randomUUID()

        val entityId = IdMapper.UUIDtoString.toEntityId(uuid)
        val modelId = IdMapper.UUIDtoString.toModelId(entityId)

        assertEquals(uuid.toString(), entityId)
        assertEquals(uuid, modelId)
    }

    @Test
    fun `UUIDtoString throws on invalid UUID`() {
        assertThrows(IllegalArgumentException::class.java) {
            IdMapper.UUIDtoString.toModelId("invalid-uuid")
        }
    }

    // ---------------------------------------------------------------
    // String ↔ String
    // ---------------------------------------------------------------

    @Test
    fun `StringToString returns same value`() {
        val input = "hello-world"

        val entityId = IdMapper.StringToString.toEntityId(input)
        val modelId = IdMapper.StringToString.toModelId(entityId)

        assertEquals(input, entityId)
        assertEquals(input, modelId)
    }
}
