package com.tondracek.myfarmer.core.repository

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.whenever
import com.tondracek.myfarmer.core.repository.firestore.FirestoreEntity
import com.tondracek.myfarmer.core.repository.request.RepositoryRequest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.util.UUID

@RunWith(MockitoJUnitRunner::class)
class BaseRepositoryTest {

    @Mock
    lateinit var core: RepositoryCore<TestEntity, String>
    lateinit var repo: BaseRepository<TestModel, UUID, TestEntity, String>

    val mapper = TestMapper()
    val idMapper = IdMapper.UUIDtoString

    @Before
    fun setup() {
        repo = TestBaseRepository(core, mapper, idMapper)
    }

    @Test
    fun `create delegates to core`() = runTest {
        val model = TestModel(value = "hello")
        val entity = mapper.toEntity(model)

        whenever(core.create(entity)).thenReturn(entity.id)

        val result = repo.create(model)

        assertThat(result).isEqualTo((model.id))
        verify(core).create(entity)
    }

    @Test
    fun `update delegates to core`() = runTest {
        val model = TestModel(value = "hello")
        val entity = mapper.toEntity(model)

        whenever(core.update(entity)).thenReturn(Unit)

        repo.update(model)

        verify(core).update(entity)
    }

    @Test
    fun `delete delegates to core`() = runTest {
        val id = UUID.randomUUID()
        val entityId = idMapper.toEntityId(id)

        whenever(core.delete(entityId)).thenReturn(Unit)

        repo.delete(id)

        verify(core).delete(entityId)
    }

    @Test
    fun `getById delegates to core`() = runTest {
        val id = UUID.randomUUID()
        val entityId = idMapper.toEntityId(id)
        val model = TestModel(id = id, value = "abc")

        val value = flowOf(mapper.toEntity(model))
        whenever(core.getById(entityId)).thenReturn(value)

        val result = repo.getById(id).first()

        assertThat(result).isEqualTo(model)
        verify(core).getById(entityId)
    }

    @Test
    fun `get delegates to core`() = runTest {
        val request = RepositoryRequest()
        val model = TestModel(value = "abc")

        val value = flowOf(listOf(mapper.toEntity(model)))
        whenever(core.get(request)).thenReturn(value)

        val result: List<TestModel> = repo.get(request).first()

        assertThat(result).isEqualTo(listOf(model))
        verify(core).get(request)
    }

    @Test
    fun `getAll delegates to core`() = runTest {
        val model1 = TestModel(value = "x")
        val model2 = TestModel(value = "y")

        val value = flowOf(listOf(mapper.toEntity(model1), mapper.toEntity(model2)))
        whenever(core.getAll()).thenReturn(value)

        val result = repo.getAll().first()

        assertThat(result).isEqualTo(listOf(model1, model2))
        verify(core).getAll()
    }

    /* Test classes */

    data class TestModel(
        val id: UUID = UUID.randomUUID(),
        val value: String
    )

    data class TestEntity(
        override var id: String = "",
        var value: String = ""
    ) : FirestoreEntity

    class TestMapper : EntityMapper<TestModel, TestEntity> {

        override fun toEntity(model: TestModel) =
            TestEntity(id = model.id.toString(), value = model.value)

        override fun toModel(entity: TestEntity) =
            TestModel(UUID.fromString(entity.id), entity.value)
    }

    class TestBaseRepository(
        core: RepositoryCore<TestEntity, String>,
        mapper: TestMapper,
        idMapper: IdMapper<UUID, String>
    ) : BaseRepository<TestModel, UUID, TestEntity, String>(
        core = core,
        entityMapper = mapper,
        idMapper = idMapper,
    )
}
