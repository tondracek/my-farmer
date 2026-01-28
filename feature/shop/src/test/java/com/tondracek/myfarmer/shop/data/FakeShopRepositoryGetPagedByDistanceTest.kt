package com.tondracek.myfarmer.shop.data

import com.google.common.truth.Truth.assertThat
import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import com.tondracek.myfarmer.location.domain.model.DistanceRing
import com.tondracek.myfarmer.location.domain.model.Location
import com.tondracek.myfarmer.openinghours.domain.model.OpeningHours
import com.tondracek.myfarmer.productmenu.domain.model.ProductMenu
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.repository.DistancePagingCursor
import com.tondracek.myfarmer.shop.domain.repository.ShopRepository
import com.tondracek.myfarmer.user.domain.model.UserId
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FakeShopRepositoryGetPagedByDistanceTest {

    private lateinit var repo: ShopRepository

    private val center = Location(latitude = 50.087, longitude = 14.421) // Prague

    private val rings = listOf(
        DistanceRing(0.0, 5_000.0),    // near
        DistanceRing(5_000.0, 20_000.0) // far
    )

    @Before
    fun setup() {
        repo = FakeShopRepository()
    }

    private fun shopAt(lat: Double, lon: Double, name: String) = Shop(
        id = ShopId.newId(),
        ownerId = UserId.newId(),
        name = name,
        location = Location(lat, lon),
        images = emptyList(),
        description = null,
        categories = emptyList(),
        menu = ProductMenu.Empty,
        openingHours = OpeningHours.Empty,
    )

    @Test
    fun `returns empty page when no rings left`() = runTest {
        val result = repo.getPagedByDistance(
            center = center,
            pageSize = 10,
            cursor = DistancePagingCursor(ringIndex = 99, afterGeohash = null),
            rings = rings
        )

        val expected = DomainResult.Success(Pair(emptyList<Shop>(), null))
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `returns shops inside first ring sorted by distance`() = runTest {
        val near1 = shopAt(50.087, 14.421, "A") // center
        val near2 = shopAt(50.09, 14.43, "B")  // slightly further
        val far = shopAt(50.2, 14.6, "C")      // far

        repo.create(near2)
        repo.create(far)
        repo.create(near1)

        val result = repo.getPagedByDistance(
            center = center,
            pageSize = 10,
            cursor = null,
            rings = rings
        )

        val (page, cursorOut) = result.getOrNull()!!

        assertThat(page)
            .containsExactly(near1, near2)
            .inOrder()

        assertThat(cursorOut!!.ringIndex)
            .isEqualTo(1) // moved to next ring
    }

    @Test
    fun `respects page size and returns cursor inside same ring`() = runTest {
        val s1 = shopAt(50.087, 14.421, "A")
        val s2 = shopAt(50.088, 14.422, "B")
        val s3 = shopAt(50.089, 14.423, "C")

        repo.create(s1)
        repo.create(s2)
        repo.create(s3)

        val result = repo.getPagedByDistance(
            center = center,
            pageSize = 2,
            cursor = null,
            rings = rings
        )

        val (page, cursorOut) = result.getOrNull()!!

        assertThat(page)
            .hasSize(2)

        assertThat(cursorOut)
            .isNotNull()

        assertThat(cursorOut!!.ringIndex)
            .isEqualTo(0) // still same ring

        assertThat(cursorOut.afterGeohash)
            .isNotNull()
    }

    @Test
    fun `cursor continues pagination within same ring`() = runTest {
        val s1 = shopAt(50.087, 14.421, "A")
        val s2 = shopAt(50.088, 14.422, "B")
        val s3 = shopAt(50.089, 14.423, "C")

        repo.create(s1)
        repo.create(s2)
        repo.create(s3)

        val first = repo.getPagedByDistance(
            center = center,
            pageSize = 2,
            cursor = null,
            rings = rings
        )

        val (_, cursor) = first.getOrNull()!!

        val second = repo.getPagedByDistance(
            center = center,
            pageSize = 2,
            cursor = cursor,
            rings = rings
        )

        val (page2, cursor2) = second.getOrNull()!!

        assertThat(page2)
            .hasSize(1)

        assertThat(cursor2!!.ringIndex)
            .isEqualTo(1) // moved to next ring
    }

    @Test
    fun `moves to next ring when current ring exhausted`() = runTest {
        val near = shopAt(50.087, 14.421, "Near")
        val far = shopAt(50.15, 14.6, "Far")

        repo.create(near)
        repo.create(far)

        val first = repo.getPagedByDistance(
            center = center,
            pageSize = 10,
            cursor = null,
            rings = rings
        )

        val (_, cursor) = first.getOrNull()!!

        val second = repo.getPagedByDistance(
            center = center,
            pageSize = 10,
            cursor = cursor,
            rings = rings
        )

        val (page2, cursor2) = second.getOrNull()!!

        assertThat(page2)
            .containsExactly(far)

        assertThat(cursor2!!.ringIndex)
            .isEqualTo(2)
    }
}
