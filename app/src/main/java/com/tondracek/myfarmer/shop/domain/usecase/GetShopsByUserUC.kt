package com.tondracek.myfarmer.shop.domain.usecase

import com.tondracek.myfarmer.auth.domain.usecase.GetLoggedInUserUC
import com.tondracek.myfarmer.core.repository.request.filterEq
import com.tondracek.myfarmer.core.repository.request.repositoryRequest
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.core.usecaseresult.toUCResult
import com.tondracek.myfarmer.shop.data.ShopEntity
import com.tondracek.myfarmer.shop.data.ShopRepository
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import com.tondracek.myfarmer.systemuser.domain.model.UserId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class GetShopsByUserUC @Inject constructor(
    private val getLoggedInUserUC: GetLoggedInUserUC,
    private val shopRepository: ShopRepository,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<UCResult<List<Shop>>> =
        getLoggedInUserUC().flatMapLatest { user: UCResult<SystemUser> ->
            val userId: UserId = user.getOrThrow().id

            val request = repositoryRequest {
                addFilter(ShopEntity::ownerId filterEq userId.toString())
            }
            shopRepository.get(request)
        }.toUCResult(userError = "Failed to load user's shops.")
}
