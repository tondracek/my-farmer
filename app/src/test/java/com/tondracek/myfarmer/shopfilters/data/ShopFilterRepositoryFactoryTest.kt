package com.tondracek.myfarmer.shopfilters.data

import org.junit.Assert.assertNotSame
import org.junit.Assert.assertSame
import org.junit.Test

class ShopFilterRepositoryFactoryTest {

    val factory = ShopFilterRepositoryFactory()

    @Test
    fun `creates new repository for new key`() {
        val repo1 = factory.createOrGet("key1")
        val repo2 = factory.createOrGet("key2")

        assertNotSame(repo1, repo2)
    }

    @Test
    fun `returns same repository for same key`() {
        val repo1 = factory.createOrGet("key1")
        val repo2 = factory.createOrGet("key1")

        assertSame(repo1, repo2)
    }
}