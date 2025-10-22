package com.tondracek.myfarmer.systemuser.data

import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.core.repository.firebase.FirebaseRepository
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser

class UserFBRepository(override val mapper: EntityMapper<SystemUser, UserEntity>) : UserRepository,
    FirebaseRepository<SystemUser, UserEntity>(UserEntity::class.java)