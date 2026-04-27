package com.tondracek.myfarmer.shop.domain.model

import java.time.DayOfWeek

sealed interface OpeningHours {

    data class Time(val dayToHours: Map<DayOfWeek, String>) : OpeningHours

    data class Message(val message: String) : OpeningHours

    companion object {
        val Empty: OpeningHours = Message("")
    }
}