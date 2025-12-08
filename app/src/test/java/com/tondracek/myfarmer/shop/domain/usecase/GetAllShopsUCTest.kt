package com.tondracek.myfarmer.shop.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.shop.data.ShopRepository
import com.tondracek.myfarmer.shop.data.shop0
import com.tondracek.myfarmer.shop.data.shop1
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shopfilters.domain.model.ShopFilters
import com.tondracek.myfarmer.shopfilters.domain.usecase.ApplyFiltersUC
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

@RunWith(MockitoJUnitRunner::class)
class GetAllShopsUCTest {

    @Mock
    lateinit var shopRepository: ShopRepository

    @Mock
    lateinit var applyFiltersUC: ApplyFiltersUC

    private lateinit var uc: GetAllShopsUC

    @Before
    fun setup() {
        uc = GetAllShopsUC(
            shopRepository = shopRepository,
            applyFiltersUC = applyFiltersUC
        )
    }

    @Test
    fun `returns empty list when repository emits empty list`() = runTest {
        // Arrange
        whenever(shopRepository.get(any())).thenReturn(flowOf(emptyList()))
        whenever(applyFiltersUC(any(), any())).thenReturn(emptyList())

        // Act
        val result = uc().first()

        // Assert
        assertThat(result).isInstanceOf(UCResult.Success::class.java)
        assertThat(result.getOrNull()).isEmpty()
    }

    @Test
    fun `applies filters to repository result`() = runTest {
        val shops = listOf(shop0, shop1)
        val filtered = listOf(shop1)

        whenever(shopRepository.get(any()))
            .thenReturn(flowOf(shops))

        whenever(applyFiltersUC(shops, ShopFilters.None))
            .thenReturn(filtered)

        val result = uc(ShopFilters.None).first()

        assertThat(result.getOrNull()).isEqualTo(filtered)
        verify(applyFiltersUC).invoke(shops, ShopFilters.None)
    }

    @Test
    fun `returns success with filtered shops`() = runTest {
        val shops = listOf(shop0)
        whenever(shopRepository.get(any())).thenReturn(flowOf(shops))
        whenever(applyFiltersUC(shops, ShopFilters.None)).thenReturn(shops)

        val result = uc().first()

        assertThat(result).isInstanceOf(UCResult.Success::class.java)
        assertThat(result.getOrNull()).containsExactly(shop0)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `flows react to repository emissions`() = runTest {
        val flow = MutableStateFlow(listOf(shop0))

        whenever(shopRepository.get(any())).thenReturn(flow)
        whenever(applyFiltersUC.invoke(any(), any()))
            .thenAnswer { invocation -> invocation.getArgument(0) }

        val emissions = mutableListOf<UCResult<List<Shop>>>()

        val job = launch {
            uc().collect { emissions.add(it) }
        }
        advanceUntilIdle()

        flow.value = listOf(shop1)
        advanceUntilIdle()

        job.cancel()

        assertThat(emissions.size).isEqualTo(2)
        assertThat(emissions[0].getOrNull()).containsExactly(shop0)
        assertThat(emissions[1].getOrNull()).containsExactly(shop1)
    }
}
