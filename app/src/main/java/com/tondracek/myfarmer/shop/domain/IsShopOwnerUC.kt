package com.tondracek.myfarmer.shop.domain

import com.tondracek.myfarmer.core.domain.UseCaseResult
import com.tondracek.myfarmer.shared.domain.model.ShopId
import com.tondracek.myfarmer.systemuser.UserId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class IsShopOwnerUC @Inject constructor(
) : (UserId, ShopId) -> Flow<UseCaseResult<Unit>> {

    override operator fun invoke(userId: UserId, shopId: ShopId): Flow<UseCaseResult<Unit>> =
        flowOf(UseCaseResult.Success(Unit))
}