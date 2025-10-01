package com.tondracek.myfarmer.demo.data

import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.demo.domain.Demo
import java.time.LocalDateTime
import java.util.UUID

object DemoEntityMapper : EntityMapper<Demo, DemoFbDto> {
    override fun toEntity(model: Demo) = DemoFbDto(
        id = model.id.toString(),
        nameeeeee = model.name,
        index = model.index,
        date = model.date.toString()
    )

    override fun toModel(entity: DemoFbDto) = Demo(
        id = UUID.fromString(entity.id),
        name = entity.nameeeeee,
        index = entity.index,
        date = LocalDateTime.parse(entity.date)
    )
}