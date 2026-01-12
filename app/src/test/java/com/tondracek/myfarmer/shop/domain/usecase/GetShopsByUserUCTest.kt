package com.tondracek.myfarmer.shop.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import com.tondracek.myfarmer.auth.domain.usecase.GetLoggedInUserUC
import com.tondracek.myfarmer.auth.domain.usecase.result.NotLoggedInUCResult
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.shop.data.shop0
import com.tondracek.myfarmer.shop.data.shop1
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.repository.ShopRepository
import com.tondracek.myfarmer.systemuser.data.user0
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
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
class GetShopsByUserUCTest {

    @Mock
    lateinit var getLoggedInUserUC: GetLoggedInUserUC

    @Mock
    lateinit var shopRepository: ShopRepository

    private lateinit var uc: GetShopsByUserUC

    private val sampleUser = user0

    @Before
    fun setup() {
        uc = GetShopsByUserUC(
            getLoggedInUserUC = getLoggedInUserUC,
            shopRepository = shopRepository
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `returns failure when user is not logged in`() = runTest {
        whenever(getLoggedInUserUC())
            .thenReturn(flowOf(NotLoggedInUCResult()))

        val result = uc().first()

        assertThat(result).isInstanceOf(UCResult.Failure::class.java)
    }

    @Test
    fun `returns shops for logged in user`() = runTest {
        whenever(getLoggedInUserUC())
            .thenReturn(flowOf(UCResult.Success(sampleUser)))

        whenever(shopRepository.getByOwnerId(any()))
            .thenReturn(flowOf(listOf(shop0, shop1)))

        val result = uc().first()

        assertThat(result).isInstanceOf(UCResult.Success::class.java)
        assertThat(result.getOrNull()).containsExactly(shop0, shop1)
    }

    @Test
    fun `returns empty list when user has no shops`() = runTest {
        whenever(getLoggedInUserUC())
            .thenReturn(flowOf(UCResult.Success(sampleUser)))

        whenever(shopRepository.getByOwnerId(any()))
            .thenReturn(flowOf(emptyList()))

        val result = uc().first()

        assertThat(result.getOrNull()).isEmpty()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `flows react to repository emissions`() = runTest {
        val userFlow = flowOf(UCResult.Success(sampleUser))
        val shopFlow = MutableStateFlow(listOf(shop0))

        whenever(getLoggedInUserUC()).thenReturn(userFlow)
        whenever(shopRepository.getByOwnerId(any())).thenReturn(shopFlow)

        val emissions = mutableListOf<UCResult<List<Shop>>>()

        val job = launch {
            uc().collect { emissions.add(it) }
        }
        advanceUntilIdle()

        shopFlow.value = listOf(shop0, shop1)
        advanceUntilIdle()

        job.cancel()

        assertThat(emissions.size).isEqualTo(2)
        assertThat(emissions[0].getOrNull()).containsExactly(shop0)
        assertThat(emissions[1].getOrNull()).containsExactly(shop0, shop1)
    }

    @Test
    fun `errors in repository flow are converted to UCResult_Failure`() = runTest {
        whenever(getLoggedInUserUC())
            .thenReturn(flowOf(UCResult.Success(sampleUser)))

        whenever(shopRepository.getByOwnerId(any()))
            .thenReturn(flow { throw RuntimeException("DB crashed") })

        val result = uc().first()

        assertThat(result).isInstanceOf(UCResult.Failure::class.java)
    }
}
