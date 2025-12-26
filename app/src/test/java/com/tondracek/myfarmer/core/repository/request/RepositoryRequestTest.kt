package com.tondracek.myfarmer.core.repository.request

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class RepositoryRequestBuilderTest {

    data class TestEntity(
        val id: Int,
        val name: String
    )

    private val f1 = TestEntity::id filterEq 10
    private val f2 = TestEntity::name filterIn listOf("A")
    private val s1 = ascending(TestEntity::id)
    private val s2 = descending(TestEntity::name)

    @Test
    fun `empty builder creates empty request`() {
        val req = repositoryRequest { }

        assertEquals(emptyList<RequestFilter>(), req.filters)
        assertEquals(emptyList<RequestSort>(), req.sorts)
        assertNull(req.limit)
        assertNull(req.offset)
    }

    @Test
    fun `addFilter adds a single filter`() {
        val req = repositoryRequest {
            addFilter(f1)
        }

        assertEquals(listOf(f1), req.filters)
    }

    @Test
    fun `addFilters vararg adds multiple filters and skips nulls`() {
        val req = repositoryRequest {
            addFilters(f1, null, f2)
        }

        assertEquals(listOf(f1, f2), req.filters)
    }

    @Test
    fun `addSort adds a single sort`() {
        val req = repositoryRequest {
            addSort(s1)
        }

        assertEquals(listOf(s1), req.sorts)
    }

    @Test
    fun `addSorts adds multiple sorts`() {
        val req = repositoryRequest {
            addSorts(s1, s2)
        }

        assertEquals(listOf(s1, s2), req.sorts)
    }

    @Test
    fun `setLimit sets limit`() {
        val req = repositoryRequest {
            setLimit(5)
        }

        assertEquals(5, req.limit)
    }

    @Test
    fun `setOffset sets offset`() {
        val req = repositoryRequest {
            setOffset(10)
        }

        assertEquals(10, req.offset)
    }

    @Test
    fun `builder builds full request correctly`() {
        val req = repositoryRequest {
            addFilter(f1)
            addFilter(f2)
            addSort(s1)
            addSort(s2)
            setLimit(3)
            setOffset(7)
        }

        assertEquals(listOf(f1, f2), req.filters)
        assertEquals(listOf(s1, s2), req.sorts)
        assertEquals(3, req.limit)
        assertEquals(7, req.offset)
    }
}
