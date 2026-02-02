package com.tondracek.myfarmer.user.domain.repository

import com.tondracek.myfarmer.auth.domain.model.AuthId
import com.tondracek.myfarmer.core.domain.domainresult.DomainResult
import com.tondracek.myfarmer.core.domain.repository.CrudRepository
import com.tondracek.myfarmer.user.domain.model.SystemUser
import com.tondracek.myfarmer.user.domain.model.UserId
import kotlinx.coroutines.flow.Flow

/**
 * Domain facing repository
 */
interface UserRepository : CrudRepository<SystemUser, UserId> {

    fun getByIds(userIds: List<UserId>): Flow<DomainResult<List<SystemUser>>>

    fun getUserByAuthId(authId: AuthId): Flow<DomainResult<SystemUser?>>
}