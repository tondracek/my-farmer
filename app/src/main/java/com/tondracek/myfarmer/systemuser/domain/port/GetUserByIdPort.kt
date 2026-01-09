package com.tondracek.myfarmer.systemuser.domain.port

import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import com.tondracek.myfarmer.systemuser.domain.model.UserId
import kotlinx.coroutines.flow.Flow

interface GetUserByIdPort {

    operator fun invoke(id: UserId): Flow<SystemUser?>
}