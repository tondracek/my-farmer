package com.tondracek.myfarmer.shop.domain.usecase

import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.result.NotShopOwnerUCResult
import com.tondracek.myfarmer.systemuser.domain.model.UserId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class IsShopOwnerUC @Inject constructor(
    private val getShopByIdUC: GetShopByIdUC,
) : (UserId, ShopId) -> Flow<UCResult<Shop>> {

    override operator fun invoke(userId: UserId, shopId: ShopId): Flow<UCResult<Shop>> =
        getShopByIdUC(shopId).map {
            it.mapSuccessFlat { shop ->
                when (shop.ownerId == userId) {
                    true -> UCResult.Success(shop)
                    false -> NotShopOwnerUCResult(userId, shopId)
                }
            }
        }
}