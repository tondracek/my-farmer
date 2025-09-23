package com.tondracek.myfarmer.demo.domain

import com.google.firebase.dataconnect.serializers.UUIDSerializer
import com.tondracek.myfarmer.repository.serializer.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class Demo(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val index: Int,
    @Serializable(with = LocalDateTimeSerializer::class)
    val date: LocalDateTime
)


