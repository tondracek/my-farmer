package com.tondracek.myfarmer.core.repository.firestore

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.tondracek.myfarmer.core.domain.repository.firestore.FirestoreQueryBuilder.applyFilters
import com.tondracek.myfarmer.core.domain.repository.firestore.FirestoreQueryBuilder.applyLimit
import com.tondracek.myfarmer.core.domain.repository.firestore.FirestoreQueryBuilder.applyOffset
import com.tondracek.myfarmer.core.domain.repository.firestore.FirestoreQueryBuilder.applySorts
import com.tondracek.myfarmer.core.domain.repository.request.FilterEq
import com.tondracek.myfarmer.core.domain.repository.request.FilterGt
import com.tondracek.myfarmer.core.domain.repository.request.FilterGte
import com.tondracek.myfarmer.core.domain.repository.request.FilterIn
import com.tondracek.myfarmer.core.domain.repository.request.FilterLt
import com.tondracek.myfarmer.core.domain.repository.request.FilterLte
import com.tondracek.myfarmer.core.domain.repository.request.ascending
import com.tondracek.myfarmer.core.domain.repository.request.descending
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.any
import org.mockito.Mockito.anyList
import org.mockito.Mockito.anyLong
import org.mockito.Mockito.anyString
import org.mockito.Mockito.inOrder
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class FirestoreQueryBuilderTest {

    data class TestEntity(
        val id: Int,
        val name: String,
        val rating: Int
    )

    // ----------------------------------------------------
    // FILTER TESTS
    // ----------------------------------------------------

    @Test
    fun `applyFilters - whereEqualTo`() {
        val query = mock(Query::class.java)
        `when`(query.whereEqualTo(anyString(), any())).thenReturn(query)

        val filter = FilterEq(TestEntity::name, "Apple")

        val result = query.applyFilters(listOf(filter))

        verify(query).whereEqualTo("name", "Apple")
        assertEquals(query, result)
    }

    @Test
    fun `applyFilters - whereIn`() {
        val query = mock(Query::class.java)
        `when`(query.whereIn(anyString(), anyList())).thenReturn(query)

        val filter = FilterIn(TestEntity::name, listOf("A", "B"))

        val result = query.applyFilters(listOf(filter))

        verify(query).whereIn("name", listOf("A", "B"))
        assertEquals(query, result)
    }

    @Test
    fun `applyFilters - whereIn with empty list should skip`() {
        val query = mock(Query::class.java)

        val filter = FilterIn(TestEntity::name, emptyList<String>())

        val result = query.applyFilters(listOf(filter))

        verify(query, never()).whereIn(anyString(), anyList())
        assertEquals(query, result)
    }

    @Test
    fun `applyFilters - greaterThan`() {
        val query = mock(Query::class.java)
        `when`(query.whereGreaterThan(anyString(), any())).thenReturn(query)

        val filter = FilterGt(TestEntity::rating, 5)

        val result = query.applyFilters(listOf(filter))

        verify(query).whereGreaterThan("rating", 5)
        assertEquals(query, result)
    }

    @Test
    fun `applyFilters - greaterThanOrEqualTo`() {
        val query = mock(Query::class.java)
        `when`(query.whereGreaterThanOrEqualTo(anyString(), any())).thenReturn(query)

        val filter = FilterGte(TestEntity::rating, 3)

        val result = query.applyFilters(listOf(filter))

        verify(query).whereGreaterThanOrEqualTo("rating", 3)
        assertEquals(query, result)
    }

    @Test
    fun `applyFilters - lessThan`() {
        val query = mock(Query::class.java)
        `when`(query.whereLessThan(anyString(), any())).thenReturn(query)

        val filter = FilterLt(TestEntity::rating, 10)

        val result = query.applyFilters(listOf(filter))

        verify(query).whereLessThan("rating", 10)
        assertEquals(query, result)
    }

    @Test
    fun `applyFilters - lessThanOrEqualTo`() {
        val query = mock(Query::class.java)
        `when`(query.whereLessThanOrEqualTo(anyString(), any())).thenReturn(query)

        val filter = FilterLte(TestEntity::rating, 10)

        val result = query.applyFilters(listOf(filter))

        verify(query).whereLessThanOrEqualTo("rating", 10)
        assertEquals(query, result)
    }

    // ----------------------------------------------------
    // SORT TESTS
    // ----------------------------------------------------

    @Test
    fun `applySorts - ascending`() {
        val query = mock(Query::class.java)
        `when`(query.orderBy(anyString(), any(Query.Direction::class.java))).thenReturn(query)

        val sort = ascending(TestEntity::name)

        val result = query.applySorts(listOf(sort))

        verify(query).orderBy("name", Query.Direction.ASCENDING)
        assertEquals(query, result)
    }

    @Test
    fun `applySorts - descending`() {
        val query = mock(Query::class.java)
        `when`(query.orderBy(anyString(), any(Query.Direction::class.java))).thenReturn(query)

        val sort = descending(TestEntity::rating)

        val result = query.applySorts(listOf(sort))

        verify(query).orderBy("rating", Query.Direction.DESCENDING)
        assertEquals(query, result)
    }

    // ----------------------------------------------------
    // LIMIT + OFFSET TESTS
    // ----------------------------------------------------

    @Test
    fun `applyLimit works`() {
        val query = mock(Query::class.java)
        `when`(query.limit(anyLong())).thenReturn(query)

        val result = query.applyLimit(5)

        verify(query).limit(5)
        assertEquals(query, result)
    }

    @Test
    fun `applyLimit null does not call limit`() {
        val query = mock(Query::class.java)

        val result = query.applyLimit(null)

        verify(query, never()).limit(anyLong())
        assertEquals(query, result)
    }

    @Test
    fun `applyOffset works`() {
        val query = mock(Query::class.java)
        val snapshot = mock(DocumentSnapshot::class.java)
        `when`(query.startAfter(snapshot)).thenReturn(query)

        val result = query.applyOffset(snapshot)

        verify(query).startAfter(snapshot)
        assertEquals(query, result)
    }

    @Test
    fun `applyOffset null does not call startAfter`() {
        val query = mock(Query::class.java)

        val result = query.applyOffset(null)

        verify(query, never()).startAfter(any())
        assertEquals(query, result)
    }

    // ----------------------------------------------------
    // MULTI-SORT TEST
    // ----------------------------------------------------

    @Test
    fun `applySorts applies multiple sorts in correct order`() {
        val query = mock(Query::class.java)

        // Query chaining: each orderBy returns the same mock for chaining
        `when`(query.orderBy(anyString(), any(Query.Direction::class.java))).thenReturn(query)

        val sorts = listOf(
            ascending(TestEntity::rating),     // 1st
            descending(TestEntity::name)       // 2nd
        )

        val result = query.applySorts(sorts)

        // Verify first sort
        verify(query).orderBy("rating", Query.Direction.ASCENDING)

        // Verify second sort
        verify(query).orderBy("name", Query.Direction.DESCENDING)

        assertEquals(query, result)
    }

    // ----------------------------------------------------
    // FILTER + SORT COMBINATION TEST
    // ----------------------------------------------------

    @Test
    fun `applyFilters + applySorts applies filter first then sort`() {
        val query = mock(Query::class.java)

        // Filter calls
        `when`(query.whereEqualTo(anyString(), any())).thenReturn(query)

        // Sort calls
        `when`(query.orderBy(anyString(), any(Query.Direction::class.java))).thenReturn(query)

        val filter = FilterEq(TestEntity::name, "Apple")
        val sort = ascending(TestEntity::rating)

        // Apply both
        val result = query
            .applyFilters(listOf(filter))
            .applySorts(listOf(sort))

        // FIRST: filter
        val inOrder = inOrder(query)
        inOrder.verify(query).whereEqualTo("name", "Apple")

        // THEN: sort
        inOrder.verify(query).orderBy("rating", Query.Direction.ASCENDING)

        assertEquals(query, result)
    }

}
