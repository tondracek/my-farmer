package com.tondracek.myfarmer.core.data.firestore.helpers

import com.google.common.truth.Truth
import com.google.firebase.firestore.Query
import org.junit.Test
import org.mockito.kotlin.mock

class FirestoreMappersTest {

    @Test
    fun `applyIfNotNull returns original when value is null`() {
        val query: Query = mock()
        val other: Query = mock()

        val result = query.applyIfNotNull<String>(null) { other }

        // should return the original instance
        Truth.assertThat(result).isSameInstanceAs(query)
    }

    @Test
    fun `applyIfNotNull calls block and returns its result when value not null`() {
        val query: Query = mock()
        val other: Query = mock()

        val result = query.applyIfNotNull("x") { other }

        Truth.assertThat(result).isSameInstanceAs(other)
    }
}