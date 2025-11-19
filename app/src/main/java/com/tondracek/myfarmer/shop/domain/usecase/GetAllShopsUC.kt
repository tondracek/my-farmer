package com.tondracek.myfarmer.shop.domain.usecase

import com.tondracek.myfarmer.core.repository.request.repositoryRequest
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.shop.data.ShopRepository
import com.tondracek.myfarmer.shop.domain.model.Shop
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllShopsUC @Inject constructor(
    private val shopRepository: ShopRepository
) {

    operator fun invoke(): Flow<UCResult<List<Shop>>> =
        shopRepository.get(repositoryRequest { })
            .map { UCResult.Success(it) }
}