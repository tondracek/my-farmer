package com.tondracek.myfarmer.systemuser.data

import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class UserEntityMapper : EntityMapper<SystemUser, UserEntity> {

    override fun toEntity(model: SystemUser): UserEntity = UserEntity(
        id = model.id.toString()
    )

    override fun mapFlowToModel(flow: Flow<UserEntity>): Flow<SystemUser> =
        flow.map { toModel(it) }

    override fun mapEntitiesFlowToModel(flow: Flow<List<UserEntity>>): Flow<List<SystemUser>> =
        flow.map { entities -> entities.map { toModel(it) } }

    fun toModel(entity: UserEntity): SystemUser = SystemUser(
        id = UUID.fromString(entity.id)
    )
}
