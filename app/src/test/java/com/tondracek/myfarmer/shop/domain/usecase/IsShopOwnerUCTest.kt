package com.tondracek.myfarmer.shop.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.whenever
import com.tondracek.myfarmer.common.usecase.result.NotFoundUCResult
import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import com.tondracek.myfarmer.shop.data.sampleShops
import com.tondracek.myfarmer.shop.domain.result.NotShopOwnerUCResult
import com.tondracek.myfarmer.systemuser.data.sampleUsers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class IsShopOwnerUCTest {

    @Mock
    lateinit var getShopByIdUC: GetShopByIdUC

    private lateinit var uc: IsShopOwnerUC

    private val user = sampleUsers.first()
    private val shop = sampleShops.first { it.ownerId == user.id }

    @Before
    fun setup() {
        uc = IsShopOwnerUC(getShopByIdUC)
    }

    @Test
    fun `returns failure when shop does not exist`() = runTest {
        whenever(getShopByIdUC(shop.id)).thenReturn(
            flowOf(NotFoundUCResult())
        )

        val result = uc(user.id, shop.id).first()

        assertThat(result).isInstanceOf(NotFoundUCResult::class.java)
    }

    @Test
    fun `returns NotShopOwnerUCResult when user is not the owner`() = runTest {
        val anotherUser = sampleUsers.find { it.id != user.id }!!

        whenever(getShopByIdUC(shop.id))
            .thenReturn(flowOf(DomainResult.Success(shop)))

        val result = uc(anotherUser.id, shop.id).first()

        println(result)
        println(shop.ownerId)
        println(anotherUser.id)
        assertThat(result).isInstanceOf(NotShopOwnerUCResult::class.java)
    }

    @Test
    fun `returns Success when user is the owner`() = runTest {
        whenever(getShopByIdUC(shop.id)).thenReturn(
            flowOf(DomainResult.Success(shop))
        )

        val result = uc(user.id, shop.id).first()

        assertThat(result).isInstanceOf(DomainResult.Success::class.java)
    }
}
