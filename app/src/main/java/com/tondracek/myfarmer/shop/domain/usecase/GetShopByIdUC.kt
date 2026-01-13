package com.tondracek.myfarmer.shop.domain.usecase

import com.tondracek.myfarmer.core.domain.domainerror.ShopError
import com.tondracek.myfarmer.core.domain.usecaseresult.UCResult
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.repository.ShopRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetShopByIdUC @Inject constructor(
    private val repository: ShopRepository,
) {

    operator fun invoke(id: ShopId?): Flow<UCResult<Shop>> =
        when (id == null) {
            false -> repository.getById(id)
            true -> flowOf(UCResult.Failure(ShopError.NotFound))
        }
}
