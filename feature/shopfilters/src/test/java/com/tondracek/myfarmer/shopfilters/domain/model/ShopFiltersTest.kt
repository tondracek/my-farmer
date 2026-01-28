package com.tondracek.myfarmer.shopfilters.domain.model

import com.google.common.truth.Truth.assertThat
import com.tondracek.myfarmer.common.color.RgbColor
import com.tondracek.myfarmer.location.domain.model.Location
import com.tondracek.myfarmer.location.domain.model.km
import com.tondracek.myfarmer.openinghours.domain.model.OpeningHours
import com.tondracek.myfarmer.productmenu.domain.model.ProductMenu
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import com.tondracek.myfarmer.user.domain.model.UserId
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ShopFiltersTest {

    private fun shop(
        name: String,
        category: String
    ): Shop = Shop(
        id = ShopId.newId(),
        name = name,
        description = "",
        ownerId = UserId.newId(),
        location = Location(0.0, 0.0),
        categories = listOf(ShopCategory(category, RgbColor.White)),
        images = emptyList(),
        menu = ProductMenu.Empty,
        openingHours = OpeningHours.Empty,
    )

    @Test
    fun `no filters returns all shops`() = runTest {
        val shops = listOf(
            shop("A", "Fruit"),
            shop("B", "Veg")
        )

        val result = ShopFilters.None.apply(
            shops = shops,
            distanceProvider = { 10.km },
            ratingProvider = { Rating(5) }
        )

        assertThat(result).containsExactlyElementsIn(shops)
    }

    @Test
    fun `filters by max distance`() = runTest {
        val shops = listOf(
            shop("Near", "Fruit"),
            shop("Far", "Fruit")
        )

        val filters = ShopFilters(
            maxDistanceKm = 5.km,
            categories = sortedSetOf(),
            minRating = Rating.ZERO
        )

        val result = filters.apply(
            shops = shops,
            distanceProvider = { shop ->
                if (shop.name == "Near") 3.km else 10.km
            },
            ratingProvider = { Rating(5) }
        )

        assertThat(result.map { it.name })
            .containsExactly("Near")
    }

    @Test
    fun `filters by category normalized`() = runTest {
        val shops = listOf(
            shop("A", "Fresh Fruit"),
            shop("B", "Vegetables")
        )

        val filters = ShopFilters(
            maxDistanceKm = null,
            categories = sortedSetOf("fresh fruit"),
            minRating = Rating.ZERO
        )

        val result = filters.apply(
            shops = shops,
            distanceProvider = { null },
            ratingProvider = { Rating(5) }
        )

        assertThat(result.map { it.name })
            .containsExactly("A")
    }

    @Test
    fun `filters by minimum rating`() = runTest {
        val shops = listOf(
            shop("Low", "Fruit"),
            shop("High", "Fruit")
        )

        val filters = ShopFilters(
            maxDistanceKm = null,
            categories = sortedSetOf(),
            minRating = Rating(4)
        )

        val result = filters.apply(
            shops = shops,
            distanceProvider = { null },
            ratingProvider = { shop ->
                if (shop.name == "High") Rating(5) else Rating(2)
            }
        )

        assertThat(result.map { it.name })
            .containsExactly("High")
    }
}
