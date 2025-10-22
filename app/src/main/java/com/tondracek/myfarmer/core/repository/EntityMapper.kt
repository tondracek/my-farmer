package com.tondracek.myfarmer.core.repository

import kotlinx.coroutines.flow.Flow

interface EntityMapper<Model, Entity> {

    fun toEntity(model: Model): Entity

    fun mapFlowToModel(flow: Flow<Entity>): Flow<Model>

    fun mapEntitiesFlowToModel(flow: Flow<List<Entity>>): Flow<List<Model>>
}