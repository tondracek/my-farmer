package com.tondracek.myfarmer.demo.data

import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.demo.domain.Demo
import java.time.LocalDateTime
import java.util.UUID

object DemoEntityMapper : EntityMapper<Demo, DemoEntity> {
    override fun toEntity(model: Demo) = DemoEntity(
        id = model.id.toString(),
        name = model.name,
        index = model.index,
        date = model.date.toString()
    )

    override fun toModel(entity: DemoEntity) = Demo(
        id = UUID.fromString(entity.id),
        name = entity.name,
        index = entity.index,
        date = LocalDateTime.parse(entity.date)
    )
}