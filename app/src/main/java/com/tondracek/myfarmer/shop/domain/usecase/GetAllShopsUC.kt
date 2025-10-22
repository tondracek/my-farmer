package com.tondracek.myfarmer.shop.domain.usecase

import com.tondracek.myfarmer.core.repository.request.RepositoryRequest
import com.tondracek.myfarmer.shop.data.ShopRepository
import com.tondracek.myfarmer.shop.domain.model.Shop
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllShopsUC @Inject constructor(
    private val shopRepository: ShopRepository
) {

    operator fun invoke(): Flow<List<Shop>> =
        shopRepository.get(RepositoryRequest.Builder().build())
}