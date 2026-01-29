package com.tondracek.myfarmer.shop.data

import com.google.common.truth.Truth.assertThat
import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import com.tondracek.myfarmer.core.domain.usecaseresult.getOrReturn
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.repository.ShopRepository
import com.tondracek.myfarmer.shop.sample.shop0
import com.tondracek.myfarmer.shop.sample.shop1
import com.tondracek.myfarmer.user.domain.model.UserId
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FakeShopRepositoryTest {

    private lateinit var repo: ShopRepository

    @Before
    fun setup() {
        repo = FakeShopRepository()
    }

    @Test
    fun `create stores shop and returns id`() = runTest {
        val result = repo.create(shop0)

        assertThat(result)
            .isEqualTo(DomainResult.Success(shop0.id))

        val stored = repo.getById(shop0.id).first()

        assertThat(stored)
            .isEqualTo(DomainResult.Success(shop0))
    }

    @Test
    fun `update replaces existing shop`() = runTest {
        repo.create(shop0)

        val updated = shop0.copy(name = "Updated")

        val result = repo.update(updated)

        assertThat(result)
            .isEqualTo(DomainResult.Success(Unit))

        val stored = repo.getById(shop0.id).first()

        assertThat(stored.getOrNull()!!.name)
            .isEqualTo("Updated")
    }

    @Test
    fun `delete removes shop`() = runTest {
        repo.create(shop0)

        val result = repo.delete(shop0.id)

        assertThat(result)
            .isEqualTo(DomainResult.Success(Unit))

        val stored = repo.getById(shop0.id).first()

        assertThat(stored)
            .isInstanceOf(DomainResult.Failure::class.java)
    }

    @Test
    fun `getAll returns empty list when repository is empty`() = runTest {
        val result = repo.getAll().first()

        assertThat(result)
            .isEqualTo(DomainResult.Success(emptyList<ShopId>()))
    }

    @Test
    fun `getAll returns all shops`() = runTest {
        repo.create(shop0)
        repo.create(shop1)

        val result = repo.getAll().first()

        assertThat(result.getOrNull())
            .containsExactly(shop0, shop1)
    }

    @Test
    fun `getByOwnerId filters shops by owner`() = runTest {
        val owner = shop0.ownerId
        val otherOwner = UserId.newId()

        val otherShop = shop1.copy(ownerId = otherOwner)

        repo.create(shop0)
        repo.create(otherShop)

        val result = repo.getByOwnerId(owner).first()

        assertThat(result.getOrNull())
            .containsExactly(shop0)
    }

    @Test
    fun `getAllPaginated returns all when limit is null`() = runTest {
        repo.create(shop0)
        repo.create(shop1)

        val result = repo.getAllPaginated(
            limit = null,
            after = null
        )

        assertThat(result.getOrNull())
            .containsExactly(shop0, shop1)
    }

    @Test
    fun `getAllPaginated respects limit`() = runTest {
        repo.create(shop0)
        repo.create(shop1)

        val result = repo.getAllPaginated(
            limit = 1,
            after = null
        )

        assertThat(result.getOrNull()).hasSize(1)
    }

    @Test
    fun `getAllPaginated respects cursor`() = runTest {
        repo.create(shop0)
        repo.create(shop1)

        val first = repo.getAllPaginated(limit = 1)
            .getOrReturn { throw Exception() }
        val cursor = first.last().id

        val result = repo.getAllPaginated(
            limit = 1,
            after = cursor
        ).getOrReturn { throw Exception() }

        val expected = listOf(shop1, shop0).sortedBy { it.id.toString() }

        assertThat(first).containsExactly(expected[0])
        assertThat(result).containsExactly(expected[1])
    }

    @Test
    fun `getById returns not found for unknown id`() = runTest {
        val unknownId = ShopId.newId()

        val result = repo.getById(unknownId).first()

        assertThat(result)
            .isInstanceOf(DomainResult.Failure::class.java)
    }
}
