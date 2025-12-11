package com.tondracek.myfarmer.shopfilters.domain.model

import androidx.compose.ui.graphics.Color
import com.google.common.truth.Truth.assertThat
import com.tondracek.myfarmer.location.model.Distance
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.shop.data.sampleShops
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class ShopFiltersApplyTest {

    private fun fakeShop(
        categories: List<String>,
        rating: Rating,
        distanceKm: Double?,
    ): Shop {
        return sampleShops.first().copy(
            id = UUID.randomUUID(),
            categories = categories.map { ShopCategory(it, Color.White) },
        ).also { shop ->
            ratingMap[shop] = rating
            distanceMap[shop] = distanceKm?.let { Distance(it) }
        }
    }

    private val ratingMap = mutableMapOf<Shop, Rating>()
    private val distanceMap = mutableMapOf<Shop, Distance?>()

    private val ratingProvider: (Shop) -> Rating = { shop -> ratingMap[shop]!! }
    private val distanceProvider: suspend (Shop) -> Distance? = { shop -> distanceMap[shop] }

    // ------------------------------------------------------------

    @Test
    fun `no filters returns all shops`() = runTest {
        val shops = listOf(
            fakeShop(listOf("Fruit"), Rating(4), 3.0),
            fakeShop(listOf("Veggies"), Rating(2), 10.0)
        )

        val result = ShopFilters.None.apply(
            shops = shops,
            distanceProvider = distanceProvider,
            ratingProvider = ratingProvider
        )

        assertThat(result).containsExactlyElementsIn(shops)
    }

    // ------------------------------------------------------------

    @Test
    fun `filters by max distance`() = runTest {
        val shopNear = fakeShop(listOf("Fruit"), Rating(3), 2.0)
        val shopFar = fakeShop(listOf("Fruit"), Rating(3), 12.0)

        val filters = ShopFilters(
            maxDistanceKm = Distance(5.0),
            categories = sortedSetOf(),
            minRating = Rating.ZERO
        )

        val result = filters.apply(
            shops = listOf(shopNear, shopFar),
            distanceProvider = distanceProvider,
            ratingProvider = ratingProvider
        )

        assertThat(result).containsExactly(shopNear)
    }

    // ------------------------------------------------------------

    @Test
    fun `shops with null distance pass maxDistance filter`() = runTest {
        val shopNullDist = fakeShop(listOf("Fruit"), Rating(3), null)
        val shopFar = fakeShop(listOf("Fruit"), Rating(3), 20.0)

        val filters = ShopFilters(
            maxDistanceKm = Distance(5.0),
            categories = sortedSetOf(),
            minRating = Rating.ZERO
        )

        val result = filters.apply(
            shops = listOf(shopNullDist, shopFar),
            distanceProvider = distanceProvider,
            ratingProvider = ratingProvider
        )

        assertThat(result).containsExactly(shopNullDist)
    }

    // ------------------------------------------------------------

    @Test
    fun `filters by category`() = runTest {
        val shopFruit = fakeShop(listOf("Fruit"), Rating(3), 1.0)
        val shopEggs = fakeShop(listOf("Eggs"), Rating(4), 1.0)

        val filters = ShopFilters(
            maxDistanceKm = null,
            categories = sortedSetOf("Fruit"),
            minRating = Rating.ZERO
        )

        val result = filters.apply(
            shops = listOf(shopFruit, shopEggs),
            distanceProvider = distanceProvider,
            ratingProvider = ratingProvider
        )

        assertThat(result).containsExactly(shopFruit)
    }

    // ------------------------------------------------------------

    @Test
    fun `filters by minimum rating`() = runTest {
        val shopBad = fakeShop(listOf("Fruit"), Rating(2), 1.0)
        val shopGood = fakeShop(listOf("Fruit"), Rating(4), 1.0)

        val filters = ShopFilters(
            maxDistanceKm = null,
            categories = sortedSetOf(),
            minRating = Rating(4)
        )

        val result = filters.apply(
            shops = listOf(shopBad, shopGood),
            distanceProvider = distanceProvider,
            ratingProvider = ratingProvider
        )

        assertThat(result).containsExactly(shopGood)
    }

    // ------------------------------------------------------------

    @Test
    fun `applies all filters together`() = runTest {
        val shopA = fakeShop(listOf("Fruit"), Rating(5), 3.0)  // → PASS
        val shopB = fakeShop(listOf("Fruit"), Rating(2), 3.0)  // FAIL rating
        val shopC = fakeShop(listOf("Eggs"), Rating(5), 3.0)   // FAIL category
        val shopD = fakeShop(listOf("Fruit"), Rating(5), 50.0) // FAIL distance

        val filters = ShopFilters(
            maxDistanceKm = Distance(10.0),
            categories = sortedSetOf("Fruit"),
            minRating = Rating(4)
        )

        val result = filters.apply(
            shops = listOf(shopA, shopB, shopC, shopD),
            distanceProvider = distanceProvider,
            ratingProvider = ratingProvider
        )

        assertThat(result).containsExactly(shopA)
    }
}
