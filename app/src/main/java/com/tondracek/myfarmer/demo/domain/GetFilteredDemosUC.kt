package com.tondracek.myfarmer.demo.domain

import com.tondracek.myfarmer.auth.domain.flow.IsShopOwnerAuthFlow
import com.tondracek.myfarmer.core.domain.UseCaseResult
import com.tondracek.myfarmer.demo.data.DemoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetFilteredDemosUC @Inject constructor(
    private val demoRepository: DemoRepository,
    private val isShopOwnerAuthFlow: IsShopOwnerAuthFlow
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(
        names: List<String>? = null,
        index: Int? = null,
    ): Flow<UseCaseResult<List<Demo>>> =
        isShopOwnerAuthFlow(shopId = "placeholder") {
            demoRepository.getFiltered(names, index)
                .map { UseCaseResult.Success(it) }
        }
}