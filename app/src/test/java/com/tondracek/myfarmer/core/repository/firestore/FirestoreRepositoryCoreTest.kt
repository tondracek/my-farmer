package com.tondracek.myfarmer.core.repository.firestore

import com.google.common.truth.Truth.assertThat
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.check
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.tondracek.myfarmer.core.repository.firestore.firestoreclient.FirestoreClient
import com.tondracek.myfarmer.core.repository.firestore.firestoreclient.FirestoreCollection
import com.tondracek.myfarmer.core.repository.firestore.firestoreclient.FirestoreDocumentRef
import com.tondracek.myfarmer.core.repository.firestore.firestoreclient.FirestoreQuery
import com.tondracek.myfarmer.core.repository.request.filterEq
import com.tondracek.myfarmer.core.repository.request.repositoryRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.UUID

@RunWith(MockitoJUnitRunner::class)
class FirestoreRepositoryCoreTest {

    private lateinit var core: FirestoreRepositoryCore<TestEntity>

    @Mock
    lateinit var firestore: FirestoreClient

    @Mock
    lateinit var collection: FirestoreCollection

    @Mock
    lateinit var document: FirestoreDocumentRef

    @Mock
    lateinit var query: FirestoreQuery

    @Before
    fun setup() {
        whenever(firestore.collection("test")).thenReturn(collection)

        core = FirestoreRepositoryCore(
            entityClass = TestEntity::class.java,
            firestore = firestore,
        )
    }

    @Test
    fun `create writes entity to firestore`() = runTest {
        val model = TestEntity(UUID.randomUUID().toString(), "hello")

        whenever(collection.document(model.id)).thenReturn(document)
        whenever(document.set(any())).thenReturn(Unit)

        val returnedId = core.create(model)

        assertThat(returnedId).isEqualTo(model.id)
        verify(document).set(check {
            assertThat((it as TestEntity).value).isEqualTo("hello")
        })
    }

    @Test
    fun `update writes updated entity`() = runTest {
        val model = TestEntity(UUID.randomUUID().toString(), "updated")

        whenever(collection.document(model.id)).thenReturn(document)

        core.update(model)

        verify(document).set(any())
    }

    @Test
    fun `delete removes document`() = runTest {
        val id = UUID.randomUUID().toString()

        whenever(collection.document(id)).thenReturn(document)

        core.delete(id)

        verify(document).delete()
    }

    @Test
    fun `getById emits mapped model`() = runTest {
        val id = UUID.randomUUID().toString()
        val snapshotFlow = MutableStateFlow(fakeSnapshot("value123"))

        whenever(collection.document(id)).thenReturn(document)
        whenever(document.snapshots()).thenReturn(snapshotFlow)

        val result = core.getById(id).first()

        assertThat(result!!.value).isEqualTo("value123")
    }


    @Test
    fun `getAll emits list of models`() = runTest {
        val snapshots = MutableStateFlow(fakeQuerySnapshot(listOf("A", "B")))

        whenever(collection.snapshots()).thenReturn(snapshots)

        val result = core.getAll().first()

        assertThat(result.map { it.value }).containsExactly("A", "B")
    }

    @Test
    fun `get applies filters sorts and limit`() = runTest {
        val req = repositoryRequest {
            addFilter(TestEntity::value filterEq "x")
            setLimit(10)
        }

        whenever(collection.applyFilters(req.filters)).thenReturn(query)
        whenever(query.applySorts(req.sorts)).thenReturn(query)
        whenever(query.applyOffset(null)).thenReturn(query)
        whenever(query.applyLimit(10)).thenReturn(query)
        val fakeSnapshot = flowOf(fakeQuerySnapshot(listOf("ok")))
        whenever(query.snapshots()).thenReturn(fakeSnapshot)

        val result = core.get(req).first()

        verify(collection).applyFilters(req.filters)
        verify(query).applySorts(req.sorts)
        verify(query).applyLimit(10)

        assertThat(result.single().value).isEqualTo("ok")
    }

    @Test(expected = Exception::class)
    fun `initialization without entity annotation throws exception`() {
        FirestoreRepositoryCore(
            entityClass = NonAnnotatedEntity::class.java,
            firestore = firestore,
        )
    }

    /* HELPERS */

    fun fakeSnapshot(value: String): DocumentSnapshot {
        val snap = mock<DocumentSnapshot>()
        whenever(snap.id).thenReturn(UUID.randomUUID().toString())
        whenever(snap.toObject(TestEntity::class.java))
            .thenReturn(TestEntity(value = value))
        return snap
    }

    fun fakeQuerySnapshot(values: List<String>): QuerySnapshot {
        val snap = mock<QuerySnapshot>()
        val docs = values.map { value ->
            fakeSnapshot(value)
        }
        whenever(snap.documents).thenReturn(docs)
        return snap
    }

    /* TEST DATA CLASSES */

    @FirestoreCollectionName("test")
    data class TestEntity(
        override var id: String = "",
        var value: String = ""
    ) : FirestoreEntity

    data class NonAnnotatedEntity(override var id: String = "") : FirestoreEntity
}
