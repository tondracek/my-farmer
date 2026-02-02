package com.tondracek.myfarmer.shop.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.tondracek.myfarmer.core.data.domainResultOf
import com.tondracek.myfarmer.core.domain.domainerror.AuthError
import com.tondracek.myfarmer.core.domain.domainerror.InputDataError
import com.tondracek.myfarmer.core.domain.domainerror.ShopError
import com.tondracek.myfarmer.core.domain.domainresult.DomainResult
import com.tondracek.myfarmer.image.data.FakePhotoStorage
import com.tondracek.myfarmer.image.model.ImageResource
import com.tondracek.myfarmer.shop.data.FakeShopRepository
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.model.ShopInput
import com.tondracek.myfarmer.shop.domain.model.toShopInput
import com.tondracek.myfarmer.shop.domain.repository.ShopRepository
import com.tondracek.myfarmer.shop.sample.sampleShops
import com.tondracek.myfarmer.user.domain.usecase.GetLoggedInUserUC
import com.tondracek.myfarmer.user.sample.sampleUsers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class UpdateShopUCTest {

    private lateinit var repo: ShopRepository
    private lateinit var photoStorage: FakePhotoStorage

    @Mock
    lateinit var getLoggedInUserUC: GetLoggedInUserUC

    private lateinit var uc: UpdateShopUC

    private val user = sampleUsers.first()
    private val shop = sampleShops.first { it.ownerId == user.id }

    @Before
    fun setup() = runTest {
        repo = FakeShopRepository()
        photoStorage = FakePhotoStorage()

        repo.create(shop)

        uc = UpdateShopUC(
            getLoggedInUser = getLoggedInUserUC,
            shopRepository = repo,
            photoStorage = photoStorage
        )
    }

    @Test
    fun `returns failure when user is not logged in`() = runTest {
        whenever(getLoggedInUserUC())
            .thenReturn(flowOf(domainResultOf(AuthError.NotLoggedIn)))

        val result = uc(shop.id, ShopInput())

        assertThat(result)
            .isEqualTo(DomainResult.Failure(AuthError.NotLoggedIn))
    }

    @Test
    fun `returns failure when shop does not exist`() = runTest {
        whenever(getLoggedInUserUC())
            .thenReturn(flowOf(domainResultOf(user)))

        val unknownId = ShopId.newId()
        val result = uc(unknownId, ShopInput())

        assertThat(result)
            .isEqualTo(DomainResult.Failure(ShopError.NotFound))
    }

    @Test
    fun `returns failure when user is not the shop owner`() = runTest {
        val otherUser = sampleUsers.last()

        whenever(getLoggedInUserUC())
            .thenReturn(flowOf(domainResultOf(otherUser)))

        val result = uc(shop.id, ShopInput())

        assertThat(result)
            .isEqualTo(DomainResult.Failure(ShopError.NotOwner))
    }

    @Test
    fun `returns failure when input is invalid`() = runTest {
        whenever(getLoggedInUserUC())
            .thenReturn(flowOf(domainResultOf(user)))

        val invalidInput = shop.toShopInput().copy(location = null)

        val result = uc(shop.id, invalidInput)

        assertThat(result)
            .isEqualTo(DomainResult.Failure(InputDataError.MissingLocationInput))
    }

    @Test
    fun `updates shop and uploads only new photos`() = runTest {
        whenever(getLoggedInUserUC())
            .thenReturn(flowOf(domainResultOf(user)))

        val imgOld1 = ImageResource("old1")
        val imgOld2 = ImageResource("old2")
        val imgNew = ImageResource("new")

        val originalShop = shop.copy(images = listOf(imgOld1, imgOld2))
        repo.update(originalShop)

        val input = originalShop
            .toShopInput()
            .copy(images = listOf(imgOld1, imgNew))

        val result = uc(originalShop.id, input)

        assertThat(result)
            .isEqualTo(DomainResult.Success(Unit))

        assertThat(photoStorage.images.values).contains(imgNew)
        assertThat(photoStorage.images.values).doesNotContain(imgOld1)
        assertThat(photoStorage.images.values).doesNotContain(imgOld2)

        val stored = repo.getById(originalShop.id).first()

        assertThat(stored.getOrNull()!!.images.toSet())
            .isEqualTo(setOf(imgOld1, imgNew))
    }

    @Test
    fun `successful update persists changes`() = runTest {
        whenever(getLoggedInUserUC())
            .thenReturn(flowOf(domainResultOf(user)))

        val input = shop.toShopInput().copy(name = "Updated name")

        val result = uc(shop.id, input)

        assertThat(result)
            .isEqualTo(DomainResult.Success(Unit))

        val updated = repo.getById(shop.id).first()

        assertThat(updated.getOrNull()!!.name)
            .isEqualTo("Updated name")
    }
}
