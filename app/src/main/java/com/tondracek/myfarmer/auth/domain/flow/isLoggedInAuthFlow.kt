package com.tondracek.myfarmer.auth.domain.flow

import com.tondracek.myfarmer.auth.domain.IsLoggedInUC
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import com.tondracek.myfarmer.systemuser.domain.model.UserId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class IsLoggedInAuthFlow @Inject constructor(
    private val isLoggedIn: IsLoggedInUC,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun <T> invoke(
        block: (UserId) -> Flow<UCResult<T>>
    ): Flow<UCResult<T>> =
        isLoggedIn().flatMapLatest { loggedInResult: UCResult<SystemUser> ->
            when (loggedInResult) {
                is UCResult.Failure -> flowOf(loggedInResult)
                is UCResult.Success -> block(loggedInResult.data.id)
            }
        }
}