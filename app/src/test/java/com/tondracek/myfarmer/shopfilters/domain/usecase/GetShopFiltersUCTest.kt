package com.tondracek.myfarmer.shopfilters.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.tondracek.myfarmer.location.domain.model.km
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.shopfilters.data.ShopFilterRepositoryFactory
import com.tondracek.myfarmer.shopfilters.domain.model.ShopFilters
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetShopFiltersUCTest {

    val factory = ShopFilterRepositoryFactory()
    private val uc = GetShopFiltersUC(filterRepositoryFactory = factory)


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `different keys return different StateFlows`() = runTest {
        val flow1 = uc("key1")
        val flow2 = uc("key2")

        val newFilters = ShopFilters(
            maxDistanceKm = 10.km,
            categories = sortedSetOf("Fruits", "Vegetables"),
            minRating = Rating(4),
        )
        factory.createOrGet("key1")
            .updateFilters(newFilters)

        advanceUntilIdle()
        assertThat(flow1).isNotEqualTo(flow2)
        assertThat(flow1.value).isEqualTo(newFilters)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `same key returns same StateFlow`() = runTest {
        val flow1 = uc("key1")
        val flow2 = uc("key1")

        val newFilters = ShopFilters(
            maxDistanceKm = 10.km,
            categories = sortedSetOf("Fruits", "Vegetables"),
            minRating = Rating(4),
        )
        factory.createOrGet("key1")
            .updateFilters(newFilters)

        advanceUntilIdle()
        assertThat(flow1).isEqualTo(flow2)
        assertThat(flow1.value).isEqualTo(flow2.value)
    }
}