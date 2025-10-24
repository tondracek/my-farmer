package com.tondracek.myfarmer.shop.domain.usecase

import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.systemuser.data.user0
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetShopOwnerUC @Inject constructor() {

    operator fun invoke(shopId: ShopId): Flow<UCResult<SystemUser>> =
        flowOf(UCResult.Success(user0)) // TODO
}