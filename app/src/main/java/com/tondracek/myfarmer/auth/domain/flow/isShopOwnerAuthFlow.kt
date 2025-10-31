package com.tondracek.myfarmer.auth.domain.flow

import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.usecase.IsShopOwnerUC
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject


class IsShopOwnerAuthFlow @Inject constructor(
    private val isLoggedInAuthFlow: IsLoggedInAuthFlow,
    private val isShopOwner: IsShopOwnerUC,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun <T> invoke(
        shopId: ShopId,
        block: () -> Flow<UCResult<T>>
    ): Flow<UCResult<T>> = isLoggedInAuthFlow { loggedUserId ->
        isShopOwner(loggedUserId, shopId).flatMapLatest { shopOwnerResult ->
            when (shopOwnerResult) {
                is UCResult.Failure -> flowOf(shopOwnerResult)
                is UCResult.Success -> block()
            }
        }
    }
}