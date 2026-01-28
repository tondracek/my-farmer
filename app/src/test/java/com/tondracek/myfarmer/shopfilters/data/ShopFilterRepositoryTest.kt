package com.tondracek.myfarmer.shopfilters.data

import com.google.common.truth.Truth.assertThat
import com.tondracek.myfarmer.location.domain.model.Distance
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.shopfilters.domain.model.ShopFilters
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ShopFilterRepositoryTest {

    @Test
    fun `initial filters is None`() {
        val repo = ShopFilterRepository()

        assertThat(repo.filters.value).isEqualTo(ShopFilters.None)
    }

    @Test
    fun `updateFilters changes the value`() {
        val repo = ShopFilterRepository()
        val expected = ShopFilters(
            maxDistanceKm = null,
            categories = sortedSetOf("Fruit"),
            minRating = Rating(3)
        )

        repo.updateFilters(expected)

        assertThat(repo.filters.value).isEqualTo(expected)
    }

    @Test
    fun `resetFilters returns to None`() {
        val repo = ShopFilterRepository()
        val nonDefault = ShopFilters(
            maxDistanceKm = Distance(5.0),
            categories = sortedSetOf("A", "B"),
            minRating = Rating(2)
        )

        repo.updateFilters(nonDefault)
        assertThat(repo.filters.value).isEqualTo(nonDefault)

        repo.resetFilters()
        assertThat(repo.filters.value).isEqualTo(ShopFilters.None)
    }

    @Test
    fun `collector receives updates`() = runTest {
        val repo = ShopFilterRepository()
        val emissions = mutableListOf<ShopFilters>()

        val job = launch {
            repo.filters.take(2).collect { emissions.add(it) }
        }

        advanceUntilIdle()

        val next = ShopFilters(
            maxDistanceKm = Distance(10.0),
            categories = sortedSetOf("Fruit"),
            minRating = Rating(4)
        )
        repo.updateFilters(next)

        advanceUntilIdle()

        job.cancel()

        assertThat(emissions.first()).isEqualTo(ShopFilters.None)
        assertThat(emissions.last()).isEqualTo(next)
    }
}
