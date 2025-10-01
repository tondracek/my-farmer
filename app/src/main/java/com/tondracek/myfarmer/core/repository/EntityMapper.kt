package com.tondracek.myfarmer.core.repository

interface EntityMapper<Model, Entity> {

    fun toEntity(model: Model): Entity

    fun toModel(entity: Entity): Model
}