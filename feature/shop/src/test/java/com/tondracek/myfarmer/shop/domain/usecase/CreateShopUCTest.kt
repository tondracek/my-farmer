package com.tondracek.myfarmer.shop.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.tondracek.myfarmer.core.domain.domainerror.AuthError
import com.tondracek.myfarmer.core.domain.domainerror.InputDataError
import com.tondracek.myfarmer.core.domain.domainresult.DomainResult
import com.tondracek.myfarmer.core.domain.domainresult.getOrElse
import com.tondracek.myfarmer.image.data.FakePhotoStorage
import com.tondracek.myfarmer.shop.data.FakeShopRepository
import com.tondracek.myfarmer.shop.domain.model.ShopInput
import com.tondracek.myfarmer.shop.domain.model.toShopInput
import com.tondracek.myfarmer.shop.domain.repository.ShopRepository
import com.tondracek.myfarmer.shop.sample.shop0
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
class CreateShopUCTest {

    private val repo: ShopRepository = FakeShopRepository()
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
        val failure = DomainResult.Failure(AuthError.NotLoggedIn)

        whenever(getLoggedInUser()).thenReturn(flowOf(failure))

        val result = uc(ShopInput.Empty)

        assertThat(result).isEqualTo(failure)
    }

    @Test
    fun `returns failure when toShop fails`() = runTest {
        val shop = shop0

        val user = sampleUsers.find { it.id == shop.ownerId }!!
        val input = shop.toShopInput().copy(location = null)
        val failure = DomainResult.Failure(InputDataError.MissingLocationInput)

        whenever(getLoggedInUser()).thenReturn(flowOf(DomainResult.Success(user)))

        val result = uc(input)

        assertThat(result).isEqualTo(failure)
    }

    @Test
    fun `photos uploaded and shop saved on success`() = runTest {
        val shop = shop0

        val user = sampleUsers.find { it.id == shop.ownerId }!!
        val input = shop.toShopInput()

        whenever(getLoggedInUser())
            .thenReturn(flowOf(DomainResult.Success(user)))

        val result = uc(input)

        assertThat(result).isInstanceOf(DomainResult.Success::class.java)

        val shops = repo.getAll()
            .first()
            .getOrElse(emptyList())

        assertThat(shops.size).isEqualTo(1)
        assertThat(photoStorage.images.size).isEqualTo(shop0.images.size)
    }
}
