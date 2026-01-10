package com.tondracek.myfarmer.shopfilters.domain.usecase

import androidx.compose.ui.graphics.Color
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.location.model.Distance
import com.tondracek.myfarmer.location.model.Location
import com.tondracek.myfarmer.location.usecase.MeasureDistanceFromMeUC
import com.tondracek.myfarmer.openinghours.domain.model.OpeningHours
import com.tondracek.myfarmer.productmenu.domain.model.ProductMenu
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.usecase.GetAverageRatingsByShopUC
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import com.tondracek.myfarmer.shopfilters.domain.model.ShopFilters
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.UUID

@RunWith(MockitoJUnitRunner::class)
class ApplyFiltersUCTest {

    @Mock
    lateinit var measureDistanceFromMeUC: MeasureDistanceFromMeUC

    @Mock
    lateinit var getAverageRatingsByShopUC: GetAverageRatingsByShopUC

    private lateinit var uc: ApplyFiltersUC

    private lateinit var shopA: Shop
    private lateinit var shopB: Shop
    private lateinit var shopC: Shop

    @Before
    fun setup() {
        uc = ApplyFiltersUC(measureDistanceFromMeUC, getAverageRatingsByShopUC)

        shopA = fakeShop(categories = listOf("Fruit"), location = Location(0.0, 0.0))
        shopB = fakeShop(categories = listOf("Eggs"), location = Location(1.0, 1.0))
        shopC = fakeShop(categories = listOf("Fruit"), location = Location(2.0, 2.0))
    }

    private fun fakeShop(categories: List<String>, location: Location): Shop =
        Shop(
            id = UUID.randomUUID(),
            name = null,
            description = null,
            ownerId = UUID.randomUUID(),
            categories = categories.map { ShopCategory(it, Color.White) },
            images = emptyList(),
            menu = ProductMenu(emptyList()),
            location = location,
            openingHours = OpeningHours.Time(emptyMap())
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `uses rating data from GetAverageRatingsByShopUC`() = runTest {
        val shops = listOf(shopA, shopB)

        whenever(getAverageRatingsByShopUC())
            .thenReturn(
                flowOf(
                    UCResult.Success(
                        mapOf(
                            shopA.id to Rating(5),
                            shopB.id to Rating(2)
                        )
                    )
                )
            )

        whenever(measureDistanceFromMeUC(any())).thenReturn(null)

        val filters = ShopFilters(
            maxDistanceKm = null,
            categories = sortedSetOf(),
            minRating = Rating(3)
        )

        val result = uc(shops, filters)

        assertThat(result).containsExactly(shopA)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `filters shops by distance`() = runTest {
        whenever(getAverageRatingsByShopUC())
            .thenReturn(flowOf(UCResult.Success(emptyMap())))

        whenever(measureDistanceFromMeUC(shopA.location)).thenReturn(Distance(3.0))
        whenever(measureDistanceFromMeUC(shopB.location)).thenReturn(Distance(15.0))

        val filters = ShopFilters(
            maxDistanceKm = Distance(10.0),
            categories = sortedSetOf(),
            minRating = Rating.ZERO
        )

        val result = uc(listOf(shopA, shopB), filters)

        assertThat(result).containsExactly(shopA)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `filters shops by category`() = runTest {
        whenever(getAverageRatingsByShopUC())
            .thenReturn(flowOf(UCResult.Success(emptyMap())))

        whenever(measureDistanceFromMeUC(any())).thenReturn(null)

        val filters = ShopFilters(
            maxDistanceKm = null,
            categories = sortedSetOf("Fruit"),
            minRating = Rating.ZERO
        )

        val result = uc(listOf(shopA, shopB), filters)

        assertThat(result).containsExactly(shopA)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `rating falls back to ZERO when shop has no rating`() = runTest {
        whenever(getAverageRatingsByShopUC())
            .thenReturn(flowOf(UCResult.Success(emptyMap())))

        whenever(measureDistanceFromMeUC(any())).thenReturn(null)

        val filters = ShopFilters(
            maxDistanceKm = null,
            categories = sortedSetOf(),
            minRating = Rating(1)
        )

        val result = uc(listOf(shopA, shopB), filters)

        assertThat(result).isEmpty()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `applies distance, category and rating filters together`() = runTest {
        whenever(getAverageRatingsByShopUC())
            .thenReturn(
                flowOf(
                    UCResult.Success(
                        mapOf(
                            shopA.id to Rating(5),
                            shopB.id to Rating(2),
                            shopC.id to Rating(5),
                        )
                    )
                )
            )

        whenever(measureDistanceFromMeUC(shopA.location))
            .thenReturn(Distance(3.0))
        whenever(measureDistanceFromMeUC(shopB.location))
            .thenReturn(Distance(3.0))
        whenever(measureDistanceFromMeUC(shopC.location))
            .thenReturn(Distance(50.0))

        val filters = ShopFilters(
            maxDistanceKm = Distance(10.0),
            categories = sortedSetOf("Fruit"),
            minRating = Rating(4)
        )

        val result = uc(listOf(shopA, shopB, shopC), filters)

        assertThat(result).containsExactly(shopA)
    }
}
