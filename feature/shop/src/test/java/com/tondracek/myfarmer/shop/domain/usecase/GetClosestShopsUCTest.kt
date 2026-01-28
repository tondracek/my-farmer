package com.tondracek.myfarmer.shop.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.tondracek.myfarmer.location.domain.model.LocationProvider
import com.tondracek.myfarmer.location.domain.usecase.measureMapDistance
import com.tondracek.myfarmer.shop.sample.shop0
import com.tondracek.myfarmer.shop.sample.shop1
import com.tondracek.myfarmer.shop.sample.shop2
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

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

        whenever(locationProvider.getCurrentLocation())
            .thenReturn(shop0.location)

        val expected = shops.sortedBy {
            measureMapDistance(shop0.location, it.location)
        }

        val result = uc(shops, count = 3)

        assertThat(result).containsExactlyElementsIn(expected).inOrder()
    }

    @Test
    fun `returns only specified count of closest shops`() = runTest {
        val shops = listOf(shop0, shop1, shop2)

        whenever(locationProvider.getCurrentLocation())
            .thenReturn(shop0.location)

        val expected = shops
            .sortedBy { measureMapDistance(shop0.location, it.location) }
            .take(2)

        val result = uc(shops, count = 2)

        assertThat(result).containsExactlyElementsIn(expected).inOrder()
    }

    @Test
    fun `returns all shops when count is greater than shop size`() = runTest {
        val shops = listOf(shop0, shop1)

        whenever(locationProvider.getCurrentLocation())
            .thenReturn(shop0.location)

        val result = uc(shops, count = 10)

        assertThat(result).containsExactlyElementsIn(shops)
    }

    @Test
    fun `gets current location exactly once`() = runTest {
        val shops = listOf(shop0, shop1)

        whenever(locationProvider.getCurrentLocation())
            .thenReturn(shop0.location)

        uc(shops, count = 2)

        verify(locationProvider, times(1)).getCurrentLocation()
    }
}
