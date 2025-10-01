package com.tondracek.myfarmer.demo.domain

import com.tondracek.myfarmer.auth.domain.IsLoggedInUC
import com.tondracek.myfarmer.core.domain.UseCaseResult
import com.tondracek.myfarmer.core.repository.request.AscendingSort
import com.tondracek.myfarmer.core.repository.request.DescendingSort
import com.tondracek.myfarmer.core.repository.request.FilterEq
import com.tondracek.myfarmer.core.repository.request.FilterIn
import com.tondracek.myfarmer.core.repository.request.RepositoryRequest
import com.tondracek.myfarmer.demo.data.DemoFBRepository
import com.tondracek.myfarmer.shared.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.IsShopOwnerUC
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetFilteredDemosUC @Inject constructor(
    private val demoRepository: DemoFBRepository,
    private val isLoggedIn: IsLoggedInUC,
    private val isShopOwner: IsShopOwnerUC,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(
        names: List<String>,
        index: Int? = null,
        shopId: ShopId = "placeholder"
    ): Flow<UseCaseResult<List<Demo>>> =
        isLoggedIn().flatMapLatest { loggedInResult ->
            loggedInResult
                .withFailure { failure -> flowOf(failure) }
                .withSuccess {
                    flowOf(
                        UseCaseResult.Success(emptyList())
                    )
                }
        }


    /*
        @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(
        names: List<String>,
        index: Int? = null,
        shopId: ShopId = "placeholder"
    ): Flow<UseCaseResult<List<Demo>>> =
        isLoggedIn().flatMapLatest { loggedInResult ->
            when (loggedInResult) {
                is UseCaseResult.Failure -> flowOf(loggedInResult)
                is UseCaseResult.Success -> {
                    val loggedUser = loggedInResult.data

                    isShopOwner(loggedUser.id, shopId).flatMapLatest { shopOwnerResult ->
                        when (shopOwnerResult) {
                            is UseCaseResult.Failure -> flowOf(shopOwnerResult)
                            is UseCaseResult.Success -> {

                                demoRepository.get(
                                    RepositoryRequest.Builder()
                                        .addFilters(
                                            FilterIn(field = Demo::name, values = names),
                                            FilterEq(field = Demo::index, value = index)
                                        )
                                        .addSorts(
                                            DescendingSort(field = Demo::index),
                                            AscendingSort(field = Demo::name)
                                        )
                                        .build()
                                ).map { UseCaseResult.Success(it) }
                            }
                        }
                    }
                }
            }
        }
     */
}