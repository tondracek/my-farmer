package com.tondracek.myfarmer.systemuser.domain.port

import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import com.tondracek.myfarmer.systemuser.domain.model.UserId
import kotlinx.coroutines.flow.Flow

interface GetUsersByIdsPort {

    operator fun invoke(ids: List<UserId>): Flow<List<SystemUser>>
}