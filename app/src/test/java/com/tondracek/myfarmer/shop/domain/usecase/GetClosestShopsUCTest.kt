package com.tondracek.myfarmer.shop.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.tondracek.myfarmer.location.model.km
import com.tondracek.myfarmer.location.usecase.MeasureDistanceFromMeUC
import com.tondracek.myfarmer.shop.data.shop0
import com.tondracek.myfarmer.shop.data.shop1
import com.tondracek.myfarmer.shop.data.shop2
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetClosestShopsUCTest {

    @Mock
    lateinit var measureDistanceFromMeUC: MeasureDistanceFromMeUC

    private lateinit var uc: GetClosestShopsUC

    @Before
    fun setup() {
        uc = GetClosestShopsUC(
            measureDistanceFromMeUC = measureDistanceFromMeUC
        )
    }

    @Test
    fun `returns empty list when input shops are empty`() = runTest {
        val result = uc(emptyList(), count = 5)

        assertThat(result).isEmpty()
        verify(measureDistanceFromMeUC, times(0)).invoke(any())
    }

    @Test
    fun `returns shops sorted by distance ascending`() = runTest {
        val shops = listOf(shop0, shop1, shop2)

        whenever(measureDistanceFromMeUC(shop0.location)).thenReturn(100.km)
        whenever(measureDistanceFromMeUC(shop1.location)).thenReturn(50.km)
        whenever(measureDistanceFromMeUC(shop2.location)).thenReturn(200.km)

        val result = uc(shops, count = 3)

        assertThat(result).containsExactly(
            shop1, // 50
            shop0, // 100
            shop2  // 200
        ).inOrder()
    }

    @Test
    fun `returns only specified count of closest shops`() = runTest {
        val shops = listOf(shop0, shop1, shop2)

        whenever(measureDistanceFromMeUC(shop0.location)).thenReturn(300.km)
        whenever(measureDistanceFromMeUC(shop1.location)).thenReturn(100.km)
        whenever(measureDistanceFromMeUC(shop2.location)).thenReturn(200.km)

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
