package com.tondracek.myfarmer.core.repository

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.whenever
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
    lateinit var core: RepositoryCore<String>  // Model = String for simplicity

    private lateinit var repo: BaseRepository<String>

    @Before
    fun setup() {
        repo = object : BaseRepository<String>(core) {}
    }

    @Test
    fun `create delegates to core`() = runTest {
        whenever(core.create("hello")).thenReturn(UUID(0, 1))

        val result = repo.create("hello")

        assertThat(result).isEqualTo(UUID(0, 1))
        verify(core).create("hello")
    }

    @Test
    fun `update delegates to core`() = runTest {
        repo.update("hello")

        verify(core).update("hello")
    }

    @Test
    fun `delete delegates to core`() = runTest {
        val id = UUID.randomUUID()

        repo.delete(id)

        verify(core).delete(id)
    }

    @Test
    fun `getById delegates to core`() = runTest {
        val id = UUID.randomUUID()
        val flow = flowOf("abc")

        whenever(core.getById(id)).thenReturn(flow)

        val result = repo.getById(id).first()

        assertThat(result).isEqualTo("abc")
        verify(core).getById(id)
    }

    @Test
    fun `get delegates to core`() = runTest {
        val request = RepositoryRequest()
        val flow = flowOf(listOf("a", "b"))

        whenever(core.get(request)).thenReturn(flow)

        val result = repo.get(request).first()

        assertThat(result).isEqualTo(listOf("a", "b"))
        verify(core).get(request)
    }

    @Test
    fun `getAll delegates to core`() = runTest {
        val flow = flowOf(listOf("x", "y"))

        whenever(core.getAll()).thenReturn(flow)

        val result = repo.getAll().first()

        assertThat(result).isEqualTo(listOf("x", "y"))
        verify(core).getAll()
    }
}
