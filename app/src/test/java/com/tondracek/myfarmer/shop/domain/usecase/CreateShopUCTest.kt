package com.tondracek.myfarmer.shop.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.whenever
import com.tondracek.myfarmer.auth.domain.usecase.GetLoggedInUserUC
import com.tondracek.myfarmer.auth.domain.usecase.result.NotLoggedInUCResult
import com.tondracek.myfarmer.common.image.data.FakePhotoStorage
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.shop.data.ShopRepository
import com.tondracek.myfarmer.shop.data.getFakeShopRepository
import com.tondracek.myfarmer.shop.data.shop0
import com.tondracek.myfarmer.shop.domain.model.ShopInput
import com.tondracek.myfarmer.shop.domain.model.toShopInput
import com.tondracek.myfarmer.shop.domain.result.MissingShopInputDataUCResult
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
class CreateShopUCTest {

    private val repo: ShopRepository = getFakeShopRepository()
    private val photoStorage = FakePhotoStorage()

    @Mock
    lateinit var getLoggedInUser: GetLoggedInUserUC

    private lateinit var uc: CreateShopUC

    @Before
    fun setup() {
        uc = CreateShopUC(
            getLoggedInUser = getLoggedInUser,
            shopRepository = repo,
            photoStorage = photoStorage
        )
    }

    @Test
    fun `returns failure when user is not logged in`() = runTest {
        val failure = NotLoggedInUCResult()

        whenever(getLoggedInUser()).thenReturn(flowOf(failure))

        val result = uc(ShopInput())

        assertThat(result).isEqualTo(failure)
    }

    @Test
    fun `returns failure when toShop fails`() = runTest {
        val shop = shop0

        val user = sampleUsers.find { it.id == shop.ownerId }!!
        val input = shop.toShopInput().copy(location = null)
        val failure = MissingShopInputDataUCResult

        whenever(getLoggedInUser())
            .thenReturn(flowOf(UCResult.Success(user)))

        val result = uc(input)

        assertThat(result).isEqualTo(failure)
    }

    @Test
    fun `photos uploaded and shop saved on success`() = runTest {
        val shop = shop0

        val user = sampleUsers.find { it.id == shop.ownerId }!!
        val input = shop.toShopInput()

        whenever(getLoggedInUser())
            .thenReturn(flowOf(UCResult.Success(user)))

        val result = uc(input)

        assertThat(result).isInstanceOf(UCResult.Success::class.java)

        val shops = repo.getAll().first()

        assertThat(shops).hasSize(1)
        assertThat(photoStorage.images).hasSize(shop0.images.size)
    }
}
