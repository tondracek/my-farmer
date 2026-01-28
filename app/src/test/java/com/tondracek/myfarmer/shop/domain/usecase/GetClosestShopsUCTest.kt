package com.tondracek.myfarmer.shop.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.tondracek.myfarmer.location.domain.model.LocationProvider
import com.tondracek.myfarmer.location.domain.model.km
import com.tondracek.myfarmer.location.domain.usecase.measureMapDistance
import com.tondracek.myfarmer.ui.common.sample.shop0
import com.tondracek.myfarmer.ui.common.sample.shop1
import com.tondracek.myfarmer.ui.common.sample.shop2
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetClosestShopsUCTest {

    @Mock
    lateinit var locationProvider: LocationProvider

    private lateinit var uc: GetClosestShopsUC

    @Before
    fun setup() {
        uc = GetClosestShopsUC(locationProvider)
    }

    @Test
    fun `returns empty list when input shops are empty`() = runTest {
        val result = uc(emptyList(), count = 5)

        assertThat(result).isEmpty()
        verify(locationProvider, times(0)).getCurrentLocation()
    }

    @Test
    fun `returns shops sorted by distance ascending`() = runTest {
        val shops = listOf(shop1, shop2, shop0)

        whenever(locationProvider.getCurrentLocation()).thenReturn(shop0.location)

        val shopsOrdered = shops.sortedBy {
            measureMapDistance(shop0.location, it.location)
        }

        val result = uc(shops, count = 3)

        assertThat(result).containsExactly(*shopsOrdered.toTypedArray()).inOrder()
    }

    @Test
    fun `returns only specified count of closest shops`() = runTest {
        val shops = listOf(shop0, shop1, shop2)

        whenever(locationProvider.getCurrentLocation()).thenReturn(shop0.location)

        shops.sortedBy {
            measureMapDistance(shop0.location, it.location)
        }

        val result = uc(shops, count = 2)

        assertThat(result).containsExactly(
            shop1,
            shop2
        ).inOrder()
    }

    @Test
    fun `returns all shops when count is greater than shop size`() = runTest {
        val shops = listOf(shop0, shop1)

        whenever(measureDistanceFromMeUC(any())).thenReturn(42.km)

        val result = uc(shops, count = 10)

        assertThat(result).containsExactlyElementsIn(shops)
    }

    @Test
    fun `measures distance for each shop exactly once`() = runTest {
        val shops = listOf(shop0, shop1)

        whenever(measureDistanceFromMeUC(any())).thenReturn(10.km)

        uc(shops, count = 2)

        verify(measureDistanceFromMeUC).invoke(shop0.location)
        verify(measureDistanceFromMeUC).invoke(shop1.location)
    }
}
