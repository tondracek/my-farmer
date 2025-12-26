package com.tondracek.myfarmer.shop.domain.usecase

import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.core.usecaseresult.mapFlowUCSuccessFlat
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.result.NotShopOwnerUCResult
import com.tondracek.myfarmer.systemuser.domain.model.UserId
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsShopOwnerUC @Inject constructor(
    private val getShopByIdUC: GetShopByIdUC,
) : (UserId, ShopId) -> Flow<UCResult<Unit>> {

    override operator fun invoke(userId: UserId, shopId: ShopId): Flow<UCResult<Unit>> =
        getShopByIdUC(shopId).mapFlowUCSuccessFlat { shop ->
            when (shop.ownerId == userId) {
                true -> UCResult.Success(Unit)
                false -> NotShopOwnerUCResult(userId, shopId)
            }
        }
}