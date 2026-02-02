package com.tondracek.myfarmer.shopfilters.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.tondracek.myfarmer.common.color.RgbColor
import com.tondracek.myfarmer.core.domain.domainresult.DomainResult
import com.tondracek.myfarmer.location.domain.model.Location
import com.tondracek.myfarmer.location.domain.usecase.GetUserLocationUC
import com.tondracek.myfarmer.openinghours.domain.model.OpeningHours
import com.tondracek.myfarmer.productmenu.domain.model.ProductMenu
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.usecase.GetAverageRatingsByShopUC
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import com.tondracek.myfarmer.shopfilters.domain.model.ShopFilters
import com.tondracek.myfarmer.user.domain.model.UserId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class ApplyShopFiltersUCTest {

    @Mock
    lateinit var getUserLocationUC: GetUserLocationUC

    @Mock
    lateinit var getAverageRatingsByShopUC: GetAverageRatingsByShopUC

    private lateinit var uc: ApplyShopFiltersUC

    private val locationFlow = MutableStateFlow<Location?>(null)
    private val ratingsFlow = MutableStateFlow<Map<ShopId, Rating>>(emptyMap())

    @Before
    fun setup() {
        whenever(getUserLocationUC())
            .thenReturn(locationFlow)

        whenever(getAverageRatingsByShopUC())
            .thenReturn(flowOf(DomainResult.Success(ratingsFlow.value)))

        uc = ApplyShopFiltersUC(
            getUserLocationUC = getUserLocationUC,
            getAverageRatingsByShopUC = getAverageRatingsByShopUC
        )
    }

    private fun shop(name: String): Shop =
        Shop(
            id = ShopId.newId(),
            ownerId = UserId.newId(),
            name = name,
            categories = listOf(
                ShopCategory(name = "Fruit", color = RgbColor.White)
            ),
            images = emptyList(),
            location = Location(0.0, 0.0),
            description = "Description of $name",
            menu = ProductMenu.Empty,
            openingHours = OpeningHours.Empty,
        )

    @Test
    fun `returns all shops when filters are None`() = runTest {
        val shops = listOf(shop("A"), shop("B"))

        locationFlow.value = Location(0.0, 0.0)
        ratingsFlow.value = emptyMap()

        val result = uc.sync(shops, ShopFilters.None)

        assertThat(result).containsExactlyElementsIn(shops)
    }
}
