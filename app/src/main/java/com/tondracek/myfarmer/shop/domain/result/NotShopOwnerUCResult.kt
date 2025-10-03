package com.tondracek.myfarmer.shop.domain.result

import com.tondracek.myfarmer.auth.domain.UserId
import com.tondracek.myfarmer.core.domain.UseCaseResult
import com.tondracek.myfarmer.shared.domain.model.ShopId

class NotShopOwnerUCResult(userId: UserId?, shopId: ShopId?) : UseCaseResult.Failure(
    userError = "You must be a shop owner to perform this action.",
    systemError = "User $userId is not an owner of shop $shopId."
)