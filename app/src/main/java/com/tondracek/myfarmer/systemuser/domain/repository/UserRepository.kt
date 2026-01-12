package com.tondracek.myfarmer.systemuser.domain.repository

import com.tondracek.myfarmer.auth.domain.model.AuthId
import com.tondracek.myfarmer.core.domain.repository.Repository
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import com.tondracek.myfarmer.systemuser.domain.model.UserId
import kotlinx.coroutines.flow.Flow

/**
 * Domain facing repository
 */
interface UserRepository : Repository<SystemUser, UserId> {

    fun getByIds(userIds: List<UserId>): Flow<List<SystemUser>>

    fun getUserByAuthId(authId: AuthId): Flow<SystemUser?>
}