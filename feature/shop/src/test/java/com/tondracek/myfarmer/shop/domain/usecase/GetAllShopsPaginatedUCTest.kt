package com.tondracek.myfarmer.shop.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.tondracek.myfarmer.core.domain.usecaseresult.getOrElse
import com.tondracek.myfarmer.shop.data.FakeShopRepository
import com.tondracek.myfarmer.shop.sample.shop0
import com.tondracek.myfarmer.shop.sample.shop1
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetAllShopsPaginatedUCTest {

    private lateinit var repo: FakeShopRepository
    private lateinit var uc: GetAllShopsPaginatedUC

    @Before
    fun setup() {
        repo = FakeShopRepository()
        uc = GetAllShopsPaginatedUC(repo)
    }

    @Test
    fun `returns all shops when no pagination is applied`() = runTest {
        repo.create(shop0)
        repo.create(shop1)

        val result = uc().getOrElse(emptyList())

        assertThat(result).containsExactly(shop0, shop1)
    }

    @Test
    fun `respects limit`() = runTest {
        repo.create(shop0)
        repo.create(shop1)

        val result = uc(limit = 1).getOrElse(emptyList())

        assertThat(result).hasSize(1)
    }

    @Test
    fun `respects cursor`() = runTest {
        repo.create(shop0)
        repo.create(shop1)
        val expected = listOf(shop0, shop1).sortedBy { it.id.toString() }

        val result = uc(after = expected[0].id)
            .getOrElse(emptyList())

        assertThat(result).containsExactly(expected[1])
    }
}
