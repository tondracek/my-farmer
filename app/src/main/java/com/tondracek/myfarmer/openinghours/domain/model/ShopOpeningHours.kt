package com.tondracek.myfarmer.openinghours.domain.model

import java.time.DayOfWeek

sealed interface ShopOpeningHours {

    data class Time(val dayToHours: Map<DayOfWeek, String>) : ShopOpeningHours

    data class Message(val message: String) : ShopOpeningHours
}