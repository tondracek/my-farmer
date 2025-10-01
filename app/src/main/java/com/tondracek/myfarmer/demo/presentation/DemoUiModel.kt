package com.tondracek.myfarmer.demo.presentation

import java.time.LocalDateTime
import java.util.UUID

data class DemoPreviewUiModel(
    val id: UUID,
    val name: String,
    val date: LocalDateTime,
    //no index in smaller preview class
)
