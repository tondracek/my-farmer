package com.tondracek.myfarmer.auth.domain.usecase

import com.tondracek.myfarmer.core.usecaseresult.UCResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class IsLoggedInUC @Inject constructor(

) : () -> Flow<UCResult<Boolean>> {

    override operator fun invoke(): Flow<UCResult<Boolean>> =
        flowOf(UCResult.Success(true))
}