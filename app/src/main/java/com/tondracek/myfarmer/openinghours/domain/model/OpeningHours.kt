package com.tondracek.myfarmer.openinghours.domain.model

import java.time.DayOfWeek

sealed interface OpeningHours {

    data class Time(val dayToHours: Map<DayOfWeek, String>) : OpeningHours

    data class Message(val message: String?) : OpeningHours
}