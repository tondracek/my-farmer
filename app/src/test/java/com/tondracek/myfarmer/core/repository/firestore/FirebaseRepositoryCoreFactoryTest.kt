package com.tondracek.myfarmer.core.repository.firestore

import com.tondracek.myfarmer.core.repository.EntityMapper
import org.junit.Assert.assertEquals
import org.junit.Test


class FirebaseRepositoryCoreFactoryTest {

    data class FakeModel(val name: String)

    data class FakeEntity(
        override var id: String,
        val name: String
    ) : FirestoreEntity

    class FakeMapper : EntityMapper<FakeModel, FakeEntity> {
        override fun toEntity(model: FakeModel): FakeEntity =
            FakeEntity(id = "1", name = model.name)

        override fun toModel(entity: FakeEntity): FakeModel =
            FakeModel(entity.name)
    }

    @Test
    fun `create injects the correct mapper and entity class`() {
        val factory = FirebaseRepositoryCoreFactory<FakeEntity>()
        val mapper = FakeMapper()

        val core = factory.create(mapper, FakeEntity::class.java)

        assertEquals(FakeEntity::class.java, core.entityClass)
        assertEquals(mapper, core.mapper)
    }
}
