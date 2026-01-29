package com.tondracek.myfarmer.shop.data

import com.google.common.truth.Truth.assertThat
import com.tondracek.myfarmer.shop.sample.shop0
import org.junit.Test

class ShopEntityTest {

    @Test
    fun `toEntity and back toModel should result in original Shop`() {
        val originalShop = shop0

        val entity = originalShop.toEntity()
        val model = entity.toModel()

        assertThat(model).isEqualTo(originalShop)
    }
}