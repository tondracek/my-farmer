package com.tondracek.myfarmer.shop.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.whenever
import com.tondracek.myfarmer.common.usecase.result.NotFoundUCResult
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.shop.data.ShopRepository
import com.tondracek.myfarmer.shop.data.shop0
import com.tondracek.myfarmer.shop.domain.model.Shop
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.UUID


@RunWith(MockitoJUnitRunner::class)
class GetShopByIdUCTest {

    @Mock
    lateinit var repository: ShopRepository

    private lateinit var uc: GetShopByIdUC

    @Before
    fun setup() {
        uc = GetShopByIdUC(repository)
    }

    @Test
    fun `returns failure when id is null`() = runTest {
        val result = uc(null).first()

        assertThat(result).isInstanceOf(NotFoundUCResult::class.java)
    }

    @Test
    fun `returns success when shop exists`() = runTest {
        val shop = shop0
        val id = shop.id

        whenever(repository.getById(id))
            .thenReturn(flowOf(shop))

        val result = uc(id).first()

        assertThat(result).isInstanceOf(UCResult.Success::class.java)
        assertThat(result.getOrNull()).isEqualTo(shop)
    }

    @Test
    fun `returns not found when repository returns null`() = runTest {
        val id = UUID.randomUUID()

        whenever(repository.getById(id))
            .thenReturn(flowOf(null))

        val result = uc(id).first()

        assertThat(result).isInstanceOf(NotFoundUCResult::class.java)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `flows react to repository emissions`() = runTest {
        val id = shop0.id

        val flow = MutableStateFlow<Shop?>(shop0)

        whenever(repository.getById(id))
            .thenReturn(flow)

        val emissions = mutableListOf<UCResult<Shop>>()

        val job = launch {
            uc(id).collect { emissions.add(it) }
        }
        advanceUntilIdle()

        flow.value = null
        advanceUntilIdle()

        job.cancel()

        assertThat(emissions[0].getOrNull()).isEqualTo(shop0)
        assertThat(emissions[1]).isInstanceOf(NotFoundUCResult::class.java)
    }
}
