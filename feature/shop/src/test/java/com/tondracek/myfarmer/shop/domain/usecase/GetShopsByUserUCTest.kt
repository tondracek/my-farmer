package com.tondracek.myfarmer.shop.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.tondracek.myfarmer.core.data.domainResultOf
import com.tondracek.myfarmer.core.domain.domainerror.AuthError
import com.tondracek.myfarmer.core.domain.domainresult.DomainResult
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.repository.ShopRepository
import com.tondracek.myfarmer.shop.sample.shop0
import com.tondracek.myfarmer.shop.sample.shop1
import com.tondracek.myfarmer.user.domain.usecase.GetLoggedInUserUC
import com.tondracek.myfarmer.user.sample.user0
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
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

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

    @Test
    fun `returns failure when user is not logged in`() = runTest {
        whenever(getLoggedInUserUC())
            .thenReturn(flowOf(domainResultOf(AuthError.NotLoggedIn)))

        val result = uc().first()

        assertThat(result)
            .isEqualTo(DomainResult.Failure(AuthError.NotLoggedIn))
    }

    @Test
    fun `returns shops for logged in user`() = runTest {
        whenever(getLoggedInUserUC())
            .thenReturn(flowOf(domainResultOf(sampleUser)))

        whenever(shopRepository.getByOwnerId(any()))
            .thenReturn(flowOf(domainResultOf(listOf(shop0, shop1))))

        val result = uc().first()

        assertThat(result)
            .isEqualTo(DomainResult.Success(listOf(shop0, shop1)))
    }

    @Test
    fun `returns empty list when user has no shops`() = runTest {
        whenever(getLoggedInUserUC())
            .thenReturn(flowOf(domainResultOf(sampleUser)))

        whenever(shopRepository.getByOwnerId(any()))
            .thenReturn(flowOf(domainResultOf(emptyList())))

        val result = uc().first()

        assertThat(result).isEqualTo(domainResultOf(emptyList<Shop>()))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `flows react to repository emissions`() = runTest {
        whenever(getLoggedInUserUC())
            .thenReturn(flowOf(domainResultOf(sampleUser)))

        val shopState = MutableStateFlow(domainResultOf(listOf(shop0)))

        whenever(shopRepository.getByOwnerId(any()))
            .thenReturn(shopState)

        val emissions = mutableListOf<DomainResult<List<Shop>>>()

        val job = launch {
            uc().collect { emissions.add(it) }
        }
        advanceUntilIdle()

        shopState.value = domainResultOf(listOf(shop0, shop1))
        advanceUntilIdle()

        job.cancel()

        assertThat(emissions).containsExactly(
            DomainResult.Success(listOf(shop0)),
            DomainResult.Success(listOf(shop0, shop1))
        ).inOrder()
    }

    @Test
    fun `repository failure is propagated`() = runTest {
        whenever(getLoggedInUserUC())
            .thenReturn(flowOf(domainResultOf(sampleUser)))

        whenever(shopRepository.getByOwnerId(any()))
            .thenReturn(flowOf(domainResultOf(AuthError.Unknown)))

        val result = uc().first()

        assertThat(result)
            .isEqualTo(DomainResult.Failure(AuthError.Unknown))
    }
}
