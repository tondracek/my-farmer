package com.tondracek.myfarmer.openinghours.data

import com.tondracek.myfarmer.openinghours.domain.model.OpeningHours
import kotlinx.serialization.Serializable
import java.time.DayOfWeek

@Serializable
data class OpeningHoursEntity(
    var type: String = "",
    var dayToHours: Map<String, String>? = null,
    var message: String? = null
)

enum class OpeningHoursType(val code: String) {
    TIME("time"),
    MESSAGE("message");
}

fun OpeningHoursEntity.toModel(): OpeningHours = when (this.type) {
    OpeningHoursType.TIME.code -> OpeningHours.Time(
        dayToHours = this.dayToHours?.mapKeys { DayOfWeek.valueOf(it.key) } ?: emptyMap()
    )

    OpeningHoursType.MESSAGE.code -> OpeningHours.Message(message = this.message ?: "")

    else -> throw IllegalArgumentException("Unknown OpeningHours type: ${this.type}")
}

fun OpeningHours.toEntity(): OpeningHoursEntity = when (this) {
    is OpeningHours.Time -> OpeningHoursEntity(
        type = OpeningHoursType.TIME.code,
        dayToHours = this.dayToHours.mapKeys { it.key.name },
        message = null
    )

    is OpeningHours.Message -> OpeningHoursEntity(
        type = OpeningHoursType.MESSAGE.code,
        dayToHours = null,
        message = this.message
    )
}