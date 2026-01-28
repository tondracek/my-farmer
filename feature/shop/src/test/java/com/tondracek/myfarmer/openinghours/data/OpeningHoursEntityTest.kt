package com.tondracek.myfarmer.openinghours.data

import com.google.common.truth.Truth.assertThat
import com.tondracek.myfarmer.openinghours.domain.model.OpeningHours
import org.junit.Test
import java.time.DayOfWeek

class OpeningHoursEntityTest {

    @Test
    fun `toEntity and toModel should be idempotent for Time type`() {
        val original = OpeningHours.Time(
            dayToHours = mapOf(
                DayOfWeek.MONDAY to "08:00 - 16:00",
                DayOfWeek.TUESDAY to "08:00 - 16:00"
            )
        )

        val entity = original.toEntity()
        val mappedBack = entity.toModel()

        assertThat(mappedBack).isEqualTo(original)
    }

    @Test
    fun `toEntity and toModel should be idempotent for Message type`() {
        val original = OpeningHours.Message(message = "Open on request")

        val entity = original.toEntity()
        val mappedBack = entity.toModel()

        assertThat(mappedBack).isEqualTo(original)
    }
}
