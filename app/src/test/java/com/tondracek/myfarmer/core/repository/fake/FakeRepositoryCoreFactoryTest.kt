package com.tondracek.myfarmer.core.repository.fake

import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.core.repository.RepositoryEntity
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.UUID

class FakeRepositoryCoreFactoryTest {

    data class FakeModel(
        val id: UUID,
        val name: String
    )

    data class FakeEntity(
        override val id: String,
        val name: String,
    ) : RepositoryEntity<String>

    class FakeMapper : EntityMapper<FakeModel, FakeEntity> {
        override fun toEntity(model: FakeModel): FakeEntity =
            FakeEntity(id = model.id.toString(), name = model.name)

        override fun toModel(entity: FakeEntity): FakeModel =
            FakeModel(id = UUID.fromString(entity.id), name = entity.name)
    }

    @Test
    fun `create injects the correct mapper and entity class`() {
        val factory = FakeRepositoryCoreFactory<FakeEntity> { UUID.fromString(this.id) }
        val mapper = FakeMapper()

        val core = factory.create(mapper, FakeEntity::class.java)

        assertEquals(mapper, core.mapper)
    }
}