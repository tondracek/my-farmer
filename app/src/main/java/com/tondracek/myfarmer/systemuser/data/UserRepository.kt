package com.tondracek.myfarmer.systemuser.data

import com.tondracek.myfarmer.core.di.RepositoryCoreFactory
import com.tondracek.myfarmer.core.repository.BaseRepository
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    factory: RepositoryCoreFactory,
    mapper: UserEntityMapper,
) : BaseRepository<SystemUser>(factory.create(mapper, UserEntity::class.java))