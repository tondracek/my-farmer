package com.tondracek.myfarmer.shop.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.tondracek.myfarmer.core.data.domainResultOf
import com.tondracek.myfarmer.core.domain.domainerror.ShopError
import com.tondracek.myfarmer.core.domain.domainresult.DomainResult
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.repository.ShopRepository
import com.tondracek.myfarmer.shop.sample.shop0
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
import org.mockito.kotlin.whenever

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
    fun `returns success when shop exists`() = runTest {
        val shop = shop0
        val id = shop.id

        whenever(repository.getById(id))
            .thenReturn(flowOf(domainResultOf(shop)))

        val result = uc(id).first()

        assertThat(result)
            .isEqualTo(DomainResult.Success(shop))
    }

    @Test
    fun `returns failure when repository returns null`() = runTest {
        val id = ShopId.newId()

        whenever(repository.getById(id))
            .thenReturn(flowOf(domainResultOf(ShopError.NotFound)))

        val result = uc(id).first()

        assertThat(result)
            .isEqualTo(DomainResult.Failure(ShopError.NotFound))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `flows react to repository emissions`() = runTest {
        val id = shop0.id
        val state = MutableStateFlow(domainResultOf(shop0))

        whenever(repository.getById(id))
            .thenReturn(state)

        val emissions = mutableListOf<DomainResult<Shop>>()

        val job = launch {
            uc(id).collect { emissions.add(it) }
        }
        advanceUntilIdle()

        state.value = domainResultOf(ShopError.NotFound)
        advanceUntilIdle()

        job.cancel()

        assertThat(emissions).containsExactly(
            DomainResult.Success(shop0),
            DomainResult.Failure(ShopError.NotFound)
        ).inOrder()
    }
}
