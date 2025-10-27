package com.tondracek.myfarmer.systemuser.data

import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserEntityMapper @Inject constructor() : EntityMapper<SystemUser, UserEntity> {
    override fun toEntity(model: SystemUser): UserEntity = model.toEntity()
    override fun toModel(entity: UserEntity): SystemUser = entity.toModel()
}
