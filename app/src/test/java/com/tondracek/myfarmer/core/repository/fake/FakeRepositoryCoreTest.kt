package com.tondracek.myfarmer.core.repository.fake

import com.google.common.truth.Truth.assertThat
import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.core.repository.RepositoryEntity
import com.tondracek.myfarmer.core.repository.request.ascending
import com.tondracek.myfarmer.core.repository.request.filterEq
import com.tondracek.myfarmer.core.repository.request.repositoryRequest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.assertNull
import java.util.UUID

data class TestModel(
    val id: UUID,
    val name: String,
    val rating: Int,
    val price: Double,
)

data class TestEntity(
    override var id: String = "",
    val name: String,
    val rating: Int,
    val price: Double,
) : RepositoryEntity<String>

class TestMapper : EntityMapper<TestModel, TestEntity> {
    override fun toEntity(model: TestModel): TestEntity =
        TestEntity(
            id = model.id.toString(),
            name = model.name,
            rating = model.rating,
            price = model.price
        )

    override fun toModel(entity: TestEntity): TestModel =
        TestModel(
            id = UUID.fromString(entity.id),
            name = entity.name,
            rating = entity.rating,
            price = entity.price
        )
}


class FakeRepositoryCoreTest {

    private val repo = FakeRepositoryCore(
        mapper = TestMapper(),
        getUUID = { UUID.fromString(id) }
    )

    @Test
    fun `create stores entity`() = runTest {
        val model = TestModel(UUID.randomUUID(), "Apple", 5, 10.0)

        val id = repo.create(model)

        assertThat(repo.items[id]!!.value.name).isEqualTo("Apple")
    }

    @Test
    fun `update modifies existing entity`() = runTest {
        val id = UUID.randomUUID()
        repo.create(TestModel(id, "Apple", 5, 10.0))

        repo.update(TestModel(id, "Updated", 7, 20.0))

        assertThat(repo.items[id]!!.value.name).isEqualTo("Updated")
        assertThat(repo.items[id]!!.value.rating).isEqualTo(7)
    }

    @Test
    fun `update ignores non existing id`() = runTest {
        val id = UUID.randomUUID()

        repo.update(TestModel(id, "Updated", 7, 20.0))

        assertNull(repo.items[id])
    }

    @Test
    fun `delete removes entity`() = runTest {
        val id = UUID.randomUUID()
        repo.create(TestModel(id, "Apple", 5, 10.0))

        repo.delete(id)

        assertThat(repo.items.containsKey(id)).isFalse()
    }

    @Test
    fun `getById returns correct model`() = runTest {
        val id = UUID.randomUUID()
        repo.create(TestModel(id, "Apple", 5, 10.0))

        val result = repo.getById(id).first()

        assertThat(result!!.name).isEqualTo("Apple")
    }

    @Test
    fun `getById returns null for missing item`() = runTest {
        val result = repo.getById(UUID.randomUUID()).first()

        assertThat(result).isNull()
    }

    @Test
    fun `getAll returns all inserted items`() = runTest {
        val a = TestModel(UUID.randomUUID(), "A", 5, 10.0)
        val b = TestModel(UUID.randomUUID(), "B", 3, 5.0)
        repo.create(a)
        repo.create(b)

        val result = repo.getAll().first()

        assertThat(result.map { it.name }).containsExactly("A", "B")
    }

    @Test
    fun `getAll returns empty list when no items`() = runTest {
        val result = repo.getAll().first()
        assertThat(result).isEmpty()
    }

    @Test
    fun `get with filter returns only matching items`() = runTest {
        val a = TestModel(UUID.randomUUID(), "Apple", 5, 10.0)
        val b = TestModel(UUID.randomUUID(), "Banana", 3, 5.0)
        repo.create(a)
        repo.create(b)

        val result = repo.get(
            repositoryRequest {
                addFilters(TestEntity::name filterEq "Banana")
            }
        ).first()

        assertThat(result).hasSize(1)
        assertThat(result.first().name).isEqualTo("Banana")
    }

    @Test
    fun `get with sorting works`() = runTest {
        val items = listOf(
            TestModel(UUID.randomUUID(), "A", 5, 10.0),
            TestModel(UUID.randomUUID(), "B", 5, 8.0),
            TestModel(UUID.randomUUID(), "C", 5, 12.0)
        )
        items.forEach { repo.create(it) }

        val result = repo.get(
            repositoryRequest {
                addSorts(ascending(TestEntity::price))
            }
        ).first()

        assertThat(result.map { it.price }).containsExactly(8.0, 10.0, 12.0).inOrder()
    }

    @Test
    fun `get with limit works`() = runTest {
        val items = listOf(
            TestModel(UUID.randomUUID(), "A", 5, 10.0),
            TestModel(UUID.randomUUID(), "B", 4, 11.0),
            TestModel(UUID.randomUUID(), "C", 3, 12.0)
        )
        items.forEach { repo.create(it) }

        val result = repo.get(
            repositoryRequest {
                setLimit(2)
            }
        ).first()

        assertThat(result).hasSize(2)
    }

    @Test
    fun `get with offset works`() = runTest {
        val items = listOf(
            TestModel(UUID.randomUUID(), "A", 5, 10.0),
            TestModel(UUID.randomUUID(), "B", 4, 11.0),
            TestModel(UUID.randomUUID(), "C", 3, 12.0)
        )
        items.forEach { repo.create(it) }

        val result = repo.get(
            repositoryRequest {
                setOffset(1)
            }
        ).first()

        assertThat(result.map { it.name }).containsExactly("B", "C")
    }
}
