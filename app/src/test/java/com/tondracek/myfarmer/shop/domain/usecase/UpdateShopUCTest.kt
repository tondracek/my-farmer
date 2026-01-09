package com.tondracek.myfarmer.shop.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.whenever
import com.tondracek.myfarmer.auth.domain.usecase.GetLoggedInUserUC
import com.tondracek.myfarmer.common.image.data.FakePhotoStorage
import com.tondracek.myfarmer.common.image.model.ImageResource
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.shop.data.ShopRepository
import com.tondracek.myfarmer.shop.data.getFakeShopRepository
import com.tondracek.myfarmer.shop.data.sampleShops
import com.tondracek.myfarmer.shop.domain.model.ShopInput
import com.tondracek.myfarmer.shop.domain.model.toShopInput
import com.tondracek.myfarmer.systemuser.data.sampleUsers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.UUID

@RunWith(MockitoJUnitRunner::class)
class UpdateShopUCTest {

    private lateinit var repo: ShopRepository
    private lateinit var photoStorage: FakePhotoStorage

    @Mock
    lateinit var getLoggedInUser: GetLoggedInUserUC

    private lateinit var uc: UpdateShopUC

    private val user = sampleUsers.first()
    private val shop = sampleShops.first { it.ownerId == user.id }

    @Before
    fun setup() = runBlocking {
        repo = getFakeShopRepository()
        photoStorage = FakePhotoStorage()

        repo.create(shop)

        uc = UpdateShopUC(
            getLoggedInUser = getLoggedInUser,
            shopRepository = repo,
            photoStorage = photoStorage
        )
    }

    @Test
    fun `returns failure when user is not logged in`() = runTest {
        whenever(getLoggedInUser()).thenReturn(flowOf(UCResult.Failure("Not logged in")))

        val result = uc(shop.id, ShopInput())

        assertThat(result).isInstanceOf(UCResult.Failure::class.java)
    }

    @Test
    fun `returns failure when shop does not exist`() = runTest {
        whenever(getLoggedInUser()).thenReturn(flowOf(UCResult.Success(user)))

        val unknownId = UUID.randomUUID()
        val result = uc(unknownId, ShopInput())

        assertThat(result).isInstanceOf(UCResult.Failure::class.java)
    }

    @Test
    fun `returns failure when user is not the shop owner`() = runTest {
        val otherUser = sampleUsers.last()

        whenever(getLoggedInUser()).thenReturn(flowOf(UCResult.Success(otherUser)))

        val result = uc(shop.id, ShopInput())

        assertThat(result).isInstanceOf(UCResult.Failure::class.java)
    }

    @Test
    fun `returns failure when toShop fails`() = runTest {
        whenever(getLoggedInUser()).thenReturn(flowOf(UCResult.Success(user)))

        val invalidInput = shop.toShopInput().copy(location = null)

        val result = uc(shop.id, invalidInput)

        assertThat(result).isInstanceOf(UCResult.Failure::class.java)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `updates shop and uploads only new photos`() = runTest {
        whenever(getLoggedInUser()).thenReturn(flowOf(UCResult.Success(user)))

        val imgOld1 = ImageResource("old1")
        val imgOld2 = ImageResource("old2")
        val imgNew3 = ImageResource("new3")

        val originalImages = setOf(imgOld1, imgOld2)

        val originalShop = shop.copy(images = originalImages.toList())
        repo.update(originalShop)

        val newImages = setOf(imgOld1, imgNew3)

        val input = originalShop.toShopInput().copy(images = newImages.toList())

        val result = uc(originalShop.id, input)

        assertThat(result).isInstanceOf(UCResult.Success::class.java)

        assertThat(photoStorage.images.containsValue(imgOld2)).isFalse()

        assertThat(photoStorage.images.containsValue(imgNew3)).isTrue()
        assertThat(photoStorage.images.containsValue(imgOld1)).isFalse()

        val stored = repo.getById(originalShop.id).first()
        assertThat(stored!!.images.toSet()).isEqualTo(newImages)
    }

    @Test
    fun `successful update returns UCResult_Success`() = runTest {
        whenever(getLoggedInUser()).thenReturn(flowOf(UCResult.Success(user)))

        val input = shop.toShopInput().copy(name = "Updated Name")

        val result = uc(shop.id, input)

        assertThat(result).isInstanceOf(UCResult.Success::class.java)

        val updated = repo.getById(shop.id).first()
        assertThat(updated!!.name).isEqualTo("Updated Name")
    }
}
