package com.tondracek.myfarmer.core.repository.fake

import com.tondracek.myfarmer.core.domain.repository.fake.FakeQueryBuilder.applyFilters
import com.tondracek.myfarmer.core.domain.repository.fake.FakeQueryBuilder.applyLimit
import com.tondracek.myfarmer.core.domain.repository.fake.FakeQueryBuilder.applyOffset
import com.tondracek.myfarmer.core.domain.repository.fake.FakeQueryBuilder.applySorts
import com.tondracek.myfarmer.core.domain.repository.request.ascending
import com.tondracek.myfarmer.core.domain.repository.request.descending
import com.tondracek.myfarmer.core.domain.repository.request.filterEq
import com.tondracek.myfarmer.core.domain.repository.request.filterGt
import com.tondracek.myfarmer.core.domain.repository.request.filterGte
import com.tondracek.myfarmer.core.domain.repository.request.filterIn
import com.tondracek.myfarmer.core.domain.repository.request.filterLt
import com.tondracek.myfarmer.core.domain.repository.request.filterLte
import org.junit.Assert.assertEquals
import org.junit.Test

class FakeQueryBuilderTest {

    data class TestEntity(
        val id: Int,
        val name: String,
        val rating: Int,
        val price: Double
    )

    private val entities = listOf(
        TestEntity(1, "Apple", 5, 10.5),
        TestEntity(2, "Banana", 3, 5.0),
        TestEntity(3, "Cherry", 4, 12.0),
        TestEntity(4, "Banana", 5, 7.0)
    )

    // -----------------------------------------
    // FILTER TESTS
    // -----------------------------------------

    @Test
    fun `FilterEq works`() {
        val result = entities.applyFilters(
            listOf(TestEntity::name filterEq "Banana")
        )
        val expected = entities.filter { it.name == "Banana" }
        assertEquals(expected, result)
    }

    @Test
    fun `FilterIn works`() {
        val result = entities.applyFilters(
            listOf(TestEntity::name filterIn listOf("Banana", "Cherry"))
        )
        val expected = entities.filter { it.name == "Banana" || it.name == "Cherry" }
        assertEquals(expected, result)
    }

    @Test
    fun `FilterGt works`() {
        val result = entities.applyFilters(
            listOf(TestEntity::rating filterGt 4)
        )
        val expected = entities.filter { it.rating > 4 }
        assertEquals(expected, result)
    }

    @Test
    fun `FilterGte works`() {
        val result = entities.applyFilters(
            listOf(TestEntity::price filterGte 7.0)
        )
        val expected = entities.filter { it.price >= 7.0 }
        assertEquals(expected, result)
    }

    @Test
    fun `FilterLt works`() {
        val result = entities.applyFilters(
            listOf(TestEntity::price filterLt 10.0)
        )
        val expected = entities.filter { it.price < 10.0 }
        assertEquals(expected, result)
    }

    @Test
    fun `FilterLte works`() {
        val result = entities.applyFilters(
            listOf(TestEntity::rating filterLte 4)
        )
        val expected = entities.filter { it.rating <= 4 }
        assertEquals(expected, result)
    }

    // -----------------------------------------
    // SORT TESTS
    // -----------------------------------------

    @Test
    fun `AscendingSort works`() {
        val result = entities.applySorts(
            listOf(ascending(TestEntity::rating))
        )
        assertEquals(listOf(entities[1], entities[2], entities[0], entities[3]), result)
    }

    @Test
    fun `DescendingSort works`() {
        val result = entities.applySorts(
            listOf(descending(TestEntity::price))
        )
        assertEquals(listOf(entities[2], entities[0], entities[3], entities[1]), result)
    }

    @Test
    fun `Multiple sorts work`() {
        val result = entities.applySorts(
            listOf(
                descending(TestEntity::rating),
                ascending(TestEntity::price)
            )
        )
        assertEquals(
            listOf(
                entities[3], // rating 5, price 7.0
                entities[0], // rating 5, price 10.5
                entities[2], // rating 4, price 12.0
                entities[1]  // rating 3, price 5.0
            ),
            result
        )
    }

    // -----------------------------------------
    // OFFSET + LIMIT TESTS
    // -----------------------------------------

    @Test
    fun `applyOffset works`() {
        val result = entities.applyOffset(2)
        assertEquals(listOf(entities[2], entities[3]), result)
    }

    @Test
    fun `applyLimit works`() {
        val result = entities.applyLimit(2)
        assertEquals(listOf(entities[0], entities[1]), result)
    }

    @Test
    fun `combined offset and limit work`() {
        val result = entities
            .applyOffset(1)
            .applyLimit(2)

        assertEquals(listOf(entities[1], entities[2]), result)
    }
}
