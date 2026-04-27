package com.tondracek.myfarmer.shop.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.tondracek.myfarmer.core.data.domainResultOf
import com.tondracek.myfarmer.core.domain.domainresult.DomainResult
import com.tondracek.myfarmer.image.data.FakePhotoStorage
import com.tondracek.myfarmer.shop.data.FakeShopRepository
import com.tondracek.myfarmer.shop.domain.repository.ShopRepository
import com.tondracek.myfarmer.shop.sample.shop0
import com.tondracek.myfarmer.user.domain.usecase.GetLoggedInUserUC
import com.tondracek.myfarmer.user.sample.sampleUsers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever


@RunWith(MockitoJUnitRunner::class)
class DeleteShopUCTest {

    private val shopRepository: ShopRepository = FakeShopRepository()

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
    fun `shop is deleted on success`() = runTest {
        val shop = shop0
        val shopId = shop.id

        shopRepository.create(shop)

        val user = sampleUsers.find { it.id == shop.ownerId }!!
        whenever(getLoggedInUserUC.sync())
            .thenReturn(domainResultOf(user))

        val shops0 = shopRepository.getAll().first()
        println(shops0)

        val result = uc(shopId)

        assertThat(result).isInstanceOf(DomainResult.Success::class.java)

        val shops = shopRepository.getAll()
            .first()
            .getOrNull()
        assertThat(shops).doesNotContain(shop)
    }
}
