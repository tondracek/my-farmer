package com.tondracek.myfarmer.shop.domain.usecase

import com.tondracek.myfarmer.common.usecase.result.NotFoundUCResult
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.shop.data.ShopRepository
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetShopByIdUC @Inject constructor(
    private val repository: ShopRepository,
) {

    operator fun invoke(id: ShopId?): Flow<UCResult<Shop>> = when (id == null) {
        false -> repository.getById(id).map {
            when (it == null) {
                false -> UCResult.Success(it)
                true -> NotFoundUCResult()
            }
        }

        true -> flowOf(NotFoundUCResult())
    }
}


