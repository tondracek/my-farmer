package com.tondracek.myfarmer.shopfilters.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.tondracek.myfarmer.shopfilters.data.ShopFilterRepositoryFactory
import org.junit.Test

class GetShopFilterRepositoryUCTest {

    private val uc = GetShopFilterRepositoryUC(
        filterRepositoryFactory = ShopFilterRepositoryFactory()
    )

    @Test
    fun `returns same repository for same key`() {
        val repo1 = uc("key")
        val repo2 = uc("key")

        assertThat(repo1).isSameInstanceAs(repo2)
    }

    @Test
    fun `returns different repositories for different keys`() {
        val repo1 = uc("key0")
        val repo2 = uc("key1")

        assertThat(repo1).isNotSameInstanceAs(repo2)
    }
}