package com.tondracek.myfarmer.systemuser.data

import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.core.repository.firebase.FirebaseRepository
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import java.util.UUID

class UserFBRepository() : UserRepository,
    FirebaseRepository<SystemUser, UserEntity>(UserEntity::class.java) {

    override val mapper: EntityMapper<SystemUser, UserEntity> =
        object : EntityMapper<SystemUser, UserEntity> {

            override fun toEntity(model: SystemUser): UserEntity = UserEntity(
                id = model.id.toString()
            )

            override fun toModel(entity: UserEntity): SystemUser = SystemUser(
                id = UUID.fromString(entity.id)
            )
        }

}