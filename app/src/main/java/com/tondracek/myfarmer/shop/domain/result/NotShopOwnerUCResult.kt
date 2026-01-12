package com.tondracek.myfarmer.shop.domain.result

import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.systemuser.domain.model.UserId

class NotShopOwnerUCResult(userId: UserId?, shopId: ShopId?) : UCResult.Failure(
    userError = "You must be a shop owner to perform this action.",
    systemError = "User $userId is not an owner of shop $shopId."
)
