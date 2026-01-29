package com.tondracek.myfarmer.user.domain.usecase

import com.tondracek.myfarmer.core.domain.domainerror.ShopError
import com.tondracek.myfarmer.core.domain.usecaseresult.DomainResult
import com.tondracek.myfarmer.user.domain.model.SystemUser
import com.tondracek.myfarmer.user.domain.model.UserId
import com.tondracek.myfarmer.user.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetUserByIdUC @Inject constructor(
    private val repository: UserRepository,
) {

    operator fun invoke(id: UserId?): Flow<DomainResult<SystemUser>> = when (id == null) {
        false -> repository.getById(id)
        true -> flowOf(DomainResult.Failure(ShopError.NotFound))
    }
}
