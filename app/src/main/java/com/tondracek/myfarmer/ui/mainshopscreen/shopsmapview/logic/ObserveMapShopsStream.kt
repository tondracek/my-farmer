package com.tondracek.myfarmer.ui.mainshopscreen.shopsmapview.logic

import com.tondracek.myfarmer.core.domain.domainresult.DomainResult
import com.tondracek.myfarmer.location.domain.model.Location
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shop.domain.repository.DistancePagingCursor
import com.tondracek.myfarmer.shop.domain.usecase.GetAllShopsPaginatedUC
import com.tondracek.myfarmer.shop.domain.usecase.GetShopsByDistancePagedUC
import com.tondracek.myfarmer.shopfilters.domain.model.ShopFilters
import com.tondracek.myfarmer.shopfilters.domain.usecase.ApplyShopFiltersUC
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ObserveMapShopsStream @Inject constructor(
    private val getShopsByDistancePaged: GetShopsByDistancePagedUC,
    private val getAllShopsPaginated: GetAllShopsPaginatedUC,
    private val applyFilters: ApplyShopFiltersUC,
) {

    operator fun invoke(
        location: Location?,
        filters: ShopFilters,
    ) = when (location) {
        null -> getAllShopsPagingFLow(filters)
        else -> getShopsByDistancePagingFlow(location, filters)
    }

    private fun getShopsByDistancePagingFlow(
        location: Location,
        filters: ShopFilters
    ): Flow<Set<Shop>> = channelFlow {
        val shops = mutableSetOf<Shop>()
        var cursor: DistancePagingCursor? = null

        do {
            val result = getShopsByDistancePaged(center = location, pageSize = 200, cursor = cursor)

            when (result) {
                is DomainResult.Success -> {
                    val (list, nextCursor) = result.data

                    val filtered = applyFilters.sync(shops = list, filters = filters)

                    shops.addAll(filtered)
                    send(shops)

                    cursor = nextCursor
                }

                is DomainResult.Failure -> break
            }
        } while (cursor != null)
    }

    private fun getAllShopsPagingFLow(filters: ShopFilters): Flow<Set<Shop>> = flow {
        val shops = mutableSetOf<Shop>()
        var cursor: ShopId? = null

        do {
            when (val result = getAllShopsPaginated(limit = 50, after = cursor)) {
                is DomainResult.Success -> {
                    val list = result.data
                    val filtered = applyFilters.sync(shops = list, filters = filters)

                    shops.addAll(filtered)
                    emit(shops)

                    cursor = list.lastOrNull()?.id
                }

                is DomainResult.Failure -> break
            }
        } while (cursor != null)
    }
}