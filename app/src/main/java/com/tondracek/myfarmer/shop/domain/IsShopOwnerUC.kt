package com.tondracek.myfarmer.shop.domain

import com.tondracek.myfarmer.auth.domain.UserId
import com.tondracek.myfarmer.core.domain.UseCaseResult
import com.tondracek.myfarmer.shared.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.result.NotShopOwnerUCResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class IsShopOwnerUC @Inject constructor(
) : (UserId, ShopId) -> Flow<UseCaseResult<Unit>> {

    override operator fun invoke(userId: UserId, shopId: ShopId): Flow<UseCaseResult<Unit>> =
        flowOf(
            if (shopId == "shop1") {
                UseCaseResult.Success(Unit)
            } else {
                NotShopOwnerUCResult(userId, shopId)
            }
        )
}