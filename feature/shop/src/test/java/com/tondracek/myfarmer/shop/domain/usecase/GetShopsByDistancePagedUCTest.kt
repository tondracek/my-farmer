package com.tondracek.myfarmer.shop.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.tondracek.myfarmer.core.domain.usecaseresult.getOrReturn
import com.tondracek.myfarmer.location.domain.model.Location
import com.tondracek.myfarmer.openinghours.domain.model.OpeningHours
import com.tondracek.myfarmer.productmenu.domain.model.ProductMenu
import com.tondracek.myfarmer.shop.data.FakeShopRepository
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.user.domain.model.UserId
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetShopsByDistancePagedUCTest {

    private lateinit var repo: FakeShopRepository
    private lateinit var uc: GetShopsByDistancePagedUC

    private val center = Location(latitude = 50.087, longitude = 14.421) // Prague

    @Before
    fun setup() {
        repo = FakeShopRepository()
        uc = GetShopsByDistancePagedUC(repo)
    }

    private fun shopAt(
        lat: Double,
        lon: Double,
        name: String,
    ): Shop =
        Shop(
            id = ShopId.newId(),
            ownerId = UserId.newId(),
            name = name,
            location = Location(lat, lon),
            images = emptyList(),
            description = "Description of $name",
            categories = emptyList(),
            menu = ProductMenu.Empty,
            openingHours = OpeningHours.Empty,
        )

    @Test
    fun `returns first page of closest shops`() = runTest {
        val near1 = shopAt(50.087, 14.421, "A") // 0 m
        val near2 = shopAt(50.088, 14.422, "B")
        val far = shopAt(50.2, 14.6, "C")

        repo.create(near2)
        repo.create(far)
        repo.create(near1)

        val (page, cursor) = uc(center = center, pageSize = 10, cursor = null)
            .getOrReturn { error("unexpected failure") }

        assertThat(page)
            .containsExactly(near1, near2)
            .inOrder()

        assertThat(cursor).isNotNull()
    }

    @Test
    fun `pagination continues with cursor`() = runTest {
        val s1 = shopAt(50.087, 14.421, "A")
        val s2 = shopAt(50.088, 14.422, "B")
        val s3 = shopAt(50.089, 14.423, "C")

        repo.create(s1)
        repo.create(s2)
        repo.create(s3)

        val (firstPage, cursor1) = uc(center = center, pageSize = 2, cursor = null)
            .getOrReturn { error("unexpected failure") }

        assertThat(firstPage).hasSize(2)
        assertThat(cursor1).isNotNull()

        val (secondPage, cursor2) = uc(center = center, pageSize = 2, cursor = cursor1)
            .getOrReturn { error("unexpected failure") }

        assertThat(secondPage).hasSize(1)
        assertThat(cursor2).isNotNull()
    }

    @Test
    fun `returns empty page when no shops exist`() = runTest {
        val (page, cursor) = uc(
            center = center,
            pageSize = 10,
            cursor = null
        ).getOrReturn { error("unexpected failure") }

        assertThat(page).isEmpty()
        assertThat(cursor).isNotNull() // advances ring
    }
}
