package com.tondracek.myfarmer.shop.domain.result

import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult
import com.tondracek.myfarmer.shop.domain.model.ShopId

data class ShopNotFoundUCResult(private val shopId: ShopId) : UCResult.Failure(
    userError = "Shop not found",
    systemError = "Shop with id $shopId was not found in the repository",
)
