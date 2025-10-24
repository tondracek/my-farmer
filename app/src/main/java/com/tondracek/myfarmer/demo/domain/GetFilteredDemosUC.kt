package com.tondracek.myfarmer.demo.domain

import com.tondracek.myfarmer.auth.domain.flow.IsShopOwnerAuthFlow
import com.tondracek.myfarmer.core.usecaseresult.UCResult
import com.tondracek.myfarmer.demo.data.DemoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class GetFilteredDemosUC @Inject constructor(
    private val demoRepository: DemoRepository,
    private val isShopOwnerAuthFlow: IsShopOwnerAuthFlow
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(
        names: List<String>? = null,
        index: Int? = null,
    ): Flow<UCResult<List<Demo>>> =
        isShopOwnerAuthFlow(shopId = UUID.randomUUID()) {
            demoRepository.getFiltered(names, index)
                .map { UCResult.Success(it) }
        }
}
