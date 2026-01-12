package com.tondracek.myfarmer.shop.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.whenever
import com.tondracek.myfarmer.auth.domain.usecase.GetLoggedInUserUC
import com.tondracek.myfarmer.auth.domain.usecase.result.NotLoggedInUCResult
import com.tondracek.myfarmer.common.image.data.FakePhotoStorage
import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult
import com.tondracek.myfarmer.shop.data.getFakeShopRepository
import com.tondracek.myfarmer.shop.data.shop0
import com.tondracek.myfarmer.shop.domain.repository.ShopRepository
import com.tondracek.myfarmer.shop.domain.result.NotShopOwnerUCResult
import com.tondracek.myfarmer.shop.domain.result.ShopNotFoundUCResult
import com.tondracek.myfarmer.systemuser.data.sampleUsers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.UUID


@RunWith(MockitoJUnitRunner::class)
class DeleteShopUCTest {

    private val shopRepository: ShopRepository = getFakeShopRepository()

    @Mock
    lateinit var getLoggedInUserUC: GetLoggedInUserUC

    private lateinit var uc: DeleteShopUC

    @Before
    fun setup() {
        uc = DeleteShopUC(
            shopRepository = shopRepository,
            photoStorage = FakePhotoStorage(),
            getLoggedInUserUC = getLoggedInUserUC,
        )
    }

    @Test
    fun `returns failure when shop does not exist`() = runTest {
        val shopId = UUID.randomUUID()

        whenever(getLoggedInUserUC())
            .thenReturn(flowOf(UCResult.of(sampleUsers.first())))

        val result = uc(shopId)

        assertThat(result).isInstanceOf(ShopNotFoundUCResult::class.java)
    }

    @Test
    fun `returns failure when user is not logged in`() = runTest {
        val shopId = UUID.randomUUID()

        whenever(getLoggedInUserUC())
            .thenReturn(flow { NotLoggedInUCResult() })

        val result = uc(shopId)

        assertThat(result).isInstanceOf(UCResult.Failure::class.java)
    }

    @Test
    fun `returns failure when logged-in user is not the owner`() = runTest {
        val shop = shop0
        val shopId = shop.id

        shopRepository.create(shop)

        val otherUser = sampleUsers.first { it.id != shop.ownerId }
        whenever(getLoggedInUserUC())
            .thenReturn(flowOf(UCResult.of(otherUser)))

        val result = uc(shopId)

        assertThat(result).isInstanceOf(NotShopOwnerUCResult::class.java)
    }

    @Test
    fun `shop is deleted on success`() = runTest {
        val shop = shop0
        val shopId = shop.id

        shopRepository.create(shop)

        val user = sampleUsers.find { it.id == shop.ownerId }!!
        whenever(getLoggedInUserUC())
            .thenReturn(flowOf(UCResult.of(user)))

        val shops0 = shopRepository.getAll().first()
        println(shops0)

        val result = uc(shopId)

        assertThat(result).isInstanceOf(UCResult.Success::class.java)

        val shops = shopRepository.getAll().first()
        println(shops)
        assertThat(shops).doesNotContain(shop)
    }
}
