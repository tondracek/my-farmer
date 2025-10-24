package com.tondracek.myfarmer.shop.domain.usecase

import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.systemuser.domain.model.UserId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class IsShopOwnerUC @Inject constructor(
) : (UserId, ShopId) -> Flow<UCResult<Unit>> {

    override operator fun invoke(userId: UserId, shopId: ShopId): Flow<UCResult<Unit>> =
        flowOf(UCResult.Success(Unit))
}